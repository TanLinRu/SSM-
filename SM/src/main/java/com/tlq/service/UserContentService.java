/**
 * @program: SSM
 * @description:文章模块
 * @author: TLQ
 * @create: 2019-03-28 19:14
 **/
package com.tlq.service;

import com.tlq.entity.Comment;
import com.tlq.entity.Page;
import com.tlq.entity.UserContent;

import java.util.List;

public interface UserContentService {
    /**
     *@Description: 查询所有Content并分页
     *@Param: [content, pageNum, pageSize]
     *@return: com.tlq.common.PageHelper.Page<com.tlq.entity.UserContent>
     *@Author: TLQ
     *@date: 2019/3/28 19:28
    **/
    Page<UserContent> findAll(UserContent content, Integer pageNum, Integer pageSize);
    Page<UserContent> findAll(UserContent content, Comment comment, Integer pageNum, Integer pageSize);
    Page<UserContent> findAllByUpvote(UserContent content, Integer pageNum, Integer pageSize);

    /**
     *@Description: 添加文章
     *@Param: [content]
     *@return: int
     *@Author: TLQ
     *@date: 2019/3/28 19:29
    **/
    public int addContent(UserContent content);

    /**
     *@Description: 根据用户id查询文章集合
     *@Param: [uid]
     *@return: java.util.List<com.tlq.entity.UserContent>
     *@Author: TLQ
     *@date: 2019/3/28 19:29
    **/
    public List<UserContent> findByUserId(Long uid);

    /**
     *@Description: 查询所有文章
     *@Param: []
     *@return: java.util.List<com.tlq.entity.UserContent>
     *@Author: TLQ
     *@date: 2019/3/28 19:30
    **/
    public List<UserContent> findAll();

    /**
     *@Description: 根据文章id查找文章
     *@Param: [id]
     *@return: com.tlq.entity.UserContent
     *@Author: TLQ
     *@date: 2019/3/28 19:30
    **/
    public UserContent findById(Long id);


    /**
     *@Description: 根据文章id更新文章
     *@Param: [content]
     *@return: void
     *@Author: TLQ
     *@date: 2019/3/28 19:31
    **/
    public void updateById(UserContent content);

    /**
     *@Description: 根据用户id查询出文章分类
     *@Param: [uid]
     *@return: java.util.List<com.tlq.entity.UserContent>
     *@Author: TLQ
     *@date: 2019/3/28 19:32
    **/
    public List<UserContent> findCategoryByUid(Long uid);


    /**
     *@Description: 根据文章分类查询所有文章
     *@Param: [category, uid, pageNum, pageSize]
     *@return: com.tlq.common.PageHelper.Page<com.tlq.entity.UserContent>
     *@Author: TLQ
     *@date: 2019/3/28 19:32
    **/
    Page<UserContent> findByCategory(String category, Long uid ,Integer pageNum, Integer pageSize);

    /**
     *@Description: 根据用户id查询所有私密文章并分页
     *@Param: [uid, pageNum, pageSize]
     *@return: com.tlq.common.PageHelper.Page<com.tlq.entity.UserContent>
     *@Author: TLQ
     *@date: 2019/3/28 19:33
    **/
    Page<UserContent> findPersonal(Long uid ,Integer pageNum, Integer pageSize);

    /**
     *@Description: 根据文章id删除文章
     *@Param: [cid]
     *@return: void
     *@Author: TLQ
     *@date: 2019/3/28 19:34
    **/
    public void deleteById(Long cid);

    /**
     *@Description: 根据发布时间倒排序并分页
     *@Param: [pageNum, pageSize]
     *@return: com.tlq.common.PageHelper.Page<com.tlq.entity.UserContent>
     *@Author: TLQ
     *@date: 2019/3/28 19:34
    **/
    Page<UserContent> findAll(Integer pageNum, Integer pageSize);
}