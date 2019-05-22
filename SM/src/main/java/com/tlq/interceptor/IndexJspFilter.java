/**
 * @program: SSM
 * @description: 首页拦截
 * @author: TLQ
 * @create: 2019-05-11 10:43
 **/
package com.tlq.interceptor;


import com.tlq.common.PageHelper;
import com.tlq.controller.BaseController;
import com.tlq.dao.UserContentMapper;
import com.tlq.entity.Page;
import com.tlq.entity.UserContent;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;
import java.util.List;


@WebFilter(filterName = "dispatcherDemoFilter",urlPatterns = "/index.jsp")
public class IndexJspFilter extends BaseController implements Filter {
    private static Logger logger = Logger.getLogger(IndexJspFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("===========自定义过滤器==========");
        ServletContext context = servletRequest.getServletContext();
        ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(context);
        UserContentMapper userContentMapper = ctx.getBean(UserContentMapper.class);
        PageHelper.startPage(null, null);//开始分页
        List<UserContent> list = userContentMapper.findByJoin(null);
        Page endPage = PageHelper.endPage();//分页结束
        servletRequest.setAttribute("page", endPage );
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}