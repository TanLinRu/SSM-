/**
 * @program: SSM
 * @description:Mybatis+Mysql 分页工具类
 * @author: TLQ
 * @create: 2019-03-28 19:18
 **/
package com.tlq.common;


import com.tlq.entity.Page;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.List;
import java.util.Properties;

@Intercepts({
        @Signature(type = StatementHandler.class, method = "prepare", args = {
                Connection.class}),
        @Signature(type = ResultSetHandler.class, method = "handleResultSets", args = {
                Statement.class})})
public class PageHelper implements Interceptor {
    private static final Logger logger = Logger.getLogger(PageHelper.class);

    public static final ThreadLocal<Page> localPage = new ThreadLocal<Page>();

    /**
     *@Description: 开始分页
     *@Param: [pageNum, pageSize]
     *@return: void
     *@Author: TLQ
     *@date: 2019/3/28 19:24
    **/
    public static void startPage(Integer pageNum, Integer pageSize) {
        System.out.println("pageNumc:" + pageNum);
        localPage.set(new Page(pageNum, pageSize));
    }

    /**
     *@Description: 结束分页并返回结果，该方法必须被调用，否则localPage会一直保存下去，直到下一次startPage
     *@Param: []
     *@return: com.tlq.common.PageHelper.Page
     *@Author: TLQ
     *@date: 2019/3/28 19:25
    **/
    public static Page endPage() {
        Page page = localPage.get();
        localPage.remove();
        return page;
    }

    //@Override
    public Object intercept(Invocation invocation) throws Throwable {
        if (localPage.get() == null) {
            return invocation.proceed();
        }
        if (invocation.getTarget() instanceof StatementHandler) {
            StatementHandler statementHandler = (StatementHandler) invocation
                    .getTarget();
            MetaObject metaStatementHandler = SystemMetaObject
                    .forObject(statementHandler);
            // 分离代理对象链(由于目标类可能被多个拦截器拦截，从而形成多次代理，通过下面的两次循环
            // 可以分离出最原始的的目标类)
            while (metaStatementHandler.hasGetter("h")) {
                Object object = metaStatementHandler.getValue("h");
                metaStatementHandler = SystemMetaObject.forObject(object);
            }
            // 分离最后一个代理对象的目标类
            while (metaStatementHandler.hasGetter("target")) {
                Object object = metaStatementHandler.getValue("target");
                metaStatementHandler = SystemMetaObject.forObject(object);
            }
            MappedStatement mappedStatement = (MappedStatement) metaStatementHandler
                    .getValue("delegate.mappedStatement");
            // 分页信息if (localPage.get() != null) {
            Page page = localPage.get();
            BoundSql boundSql = (BoundSql) metaStatementHandler
                    .getValue("delegate.boundSql");
            // 分页参数作为参数对象parameterObject的一个属性
            String sql = boundSql.getSql();

            Connection connection = (Connection) invocation.getArgs()[0];
            // 重设分页参数里的总页数等
            setPageParameter(sql, connection, mappedStatement, boundSql, page);

            System.out.println("total:" + page.getPages());

            // 如果当前页大于总页数，则等于总页数
            if (page.getPageNum() > page.getPages()) {
                page.setPageNum(page.getPages());
            }

            // 重写sql
            String pageSql = buildPageSql(sql, page);
            System.out.println("sql::::" + pageSql);

            // 重写分页sql
            metaStatementHandler.setValue("delegate.boundSql.sql", pageSql);

            // 将执行权交给下一个拦截器
            return invocation.proceed();
        } else if (invocation.getTarget() instanceof ResultSetHandler) {
            Object result = invocation.proceed();
            Page page = localPage.get();
            page.setResult((List) result);
            return result;
        }
        return null;
    }

    /**
     * 只拦截这两种类型的 <br>
     * StatementHandler <br>
     * ResultSetHandler
     *
     * @param target
     * @return
     */
    //@Override
    public Object plugin(Object target) {
        if (target instanceof StatementHandler
                || target instanceof ResultSetHandler) {
            return Plugin.wrap(target, this);
        } else {
            return target;
        }
    }

    //@Override
    public void setProperties(Properties properties) {

    }

    /**
     *@Description: 修改原SQL为分页SQL
     *@Param: [sql, page]
     *@return: java.lang.String
     *@Author: TLQ
     *@date: 2019/3/28 19:25
    **/
    private String buildPageSql(String sql, Page page) {
        StringBuilder pageSql = new StringBuilder(200);
        pageSql.append(sql);
        pageSql.append(" limit ").append(page.getStartRow());
        pageSql.append(" , ").append(page.getPageSize());
        return pageSql.toString();
    }

    /**
     *@Description: 获取总记录数
     *@Param: [sql, connection, mappedStatement, boundSql, page]
     *@return: void
     *@Author: TLQ
     *@date: 2019/3/28 19:24
    **/
    private void setPageParameter(String sql, Connection connection,
                                  MappedStatement mappedStatement, BoundSql boundSql, Page page) {
        // 记录总记录数
        String countSql = "select count(0) from (" + sql + ") as total";
        PreparedStatement countStmt = null;
        ResultSet rs = null;
        try {
            countStmt = connection.prepareStatement(countSql);
            BoundSql countBS = new BoundSql(mappedStatement.getConfiguration(),
                    countSql, boundSql.getParameterMappings(),
                    boundSql.getParameterObject());

            //
            for (ParameterMapping parameterMapping : boundSql
                    .getParameterMappings()) {
                String property = parameterMapping.getProperty();
                if (boundSql.hasAdditionalParameter(property)) {
                    countBS.setAdditionalParameter(property,
                            boundSql.getAdditionalParameter(property));
                }
            }
            //

            setParameters(countStmt, mappedStatement, countBS,
                    boundSql.getParameterObject());
            rs = countStmt.executeQuery();
            int totalCount = 0;
            if (rs.next()) {
                totalCount = rs.getInt(1);
            }
            page.setTotal(totalCount);

        } catch (SQLException e) {
            logger.error("Ignore this exception", e);
        } finally {
            try {
                if (rs != null)
                    rs.close();
            } catch (SQLException e) {
                logger.error("Ignore this exception", e);
            }
            try {
                countStmt.close();
            } catch (SQLException e) {
                logger.error("Ignore this exception", e);
            }
        }
    }

    /**
     *@Description: 代入参数值
     *@Param: [ps, mappedStatement, boundSql, parameterObject]
     *@return: void
     *@Author: TLQ
     *@date: 2019/3/28 19:26
    **/
    private void setParameters(PreparedStatement ps,
                               MappedStatement mappedStatement, BoundSql boundSql,
                               Object parameterObject) throws SQLException {

        ParameterHandler parameterHandler = new DefaultParameterHandler(
                mappedStatement, parameterObject, boundSql);
        parameterHandler.setParameters(ps);
    }

}