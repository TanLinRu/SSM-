/**
 * @program: SSM
 * @description:
 * @author: TLQ
 * @create: 2019-03-28 19:45
 **/
package com.tlq.service.impl;

import com.tlq.common.PageHelper;
import com.tlq.dao.UserContentMapper;
import com.tlq.entity.Comment;
import com.tlq.entity.Page;
import com.tlq.entity.UserContent;
import com.tlq.service.UserContentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserContentServiceImpl implements UserContentService {
    @Autowired
    private UserContentMapper userContentMapper;

    @Override
    public Page<UserContent> findAll(UserContent content, Integer pageNum, Integer pageSize) {
        //分页查询
        System.out.println("第"+pageNum+"页");
        System.out.println("每页显示："+pageSize+"条");
        PageHelper.startPage(pageNum, pageSize);//开始分页
        List<UserContent> list = userContentMapper.findByJoin(content);
        Page endPage = PageHelper.endPage();//分页结束
        List<UserContent> result = endPage.getResult();
        return endPage;
    }

    @Override
    public Page<UserContent> findAll(UserContent content, Comment comment, Integer pageNum, Integer pageSize) {
        return null;
    }

    @Override
    public Page<UserContent> findAllByUpvote(UserContent content, Integer pageNum, Integer pageSize) {
        return null;
    }

    @Override
    public int addContent(UserContent content) {
        return userContentMapper.inserContent(content);
    }

    @Override
    public List<UserContent> findByUserId(Long uid) {
        UserContent userContent = new UserContent();
        userContent.setId( uid );
        List<UserContent> list = userContentMapper.findByJoin(userContent);
        if(list!=null && list.size()>0){
            return (List<UserContent>) list.get(0);
        }else {
            return null;
        }
    }

    @Override
    public List<UserContent> findAll() {
        return userContentMapper.select( null );
    }

    @Override
    public UserContent findById(Long id) {
        return null;
    }

    @Override
    public void updateById(UserContent content) {

    }

    @Override
    public List<UserContent> findCategoryByUid(Long uid) {
        return userContentMapper.findCategoryByUid(uid);
    }

    @Override
    public Page<UserContent> findByCategory(String category, Long uid, Integer pageNum, Integer pageSize) {
        UserContent userContent = new UserContent();
        if(StringUtils.isNotBlank(category) && !"null".equals(category)){
            userContent.setCategory(category);
        }
        userContent.setuId(uid);
        userContent.setPersonal("0");
        PageHelper.startPage(pageNum, pageSize);//开始分页
        userContentMapper.select(userContent);
        Page endPage = PageHelper.endPage();//分页结束
        return endPage;
    }

    @Override
    public Page<UserContent> findPersonal(Long uid, Integer pageNum, Integer pageSize) {
        UserContent userContent = new UserContent();
        userContent.setuId(uid);
        userContent.setPersonal("1");
        PageHelper.startPage(pageNum, pageSize);//开始分页
        userContentMapper.select(userContent);
        Page endPage = PageHelper.endPage();//分页结束
        return endPage;
    }

    @Override
    public void deleteById(Long cid) {
        userContentMapper.deleteByPrimaryKey(cid);
    }

    @Override
    public Page<UserContent> findAll(Integer pageNum, Integer pageSize) {
        return null;
    }
}