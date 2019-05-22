/**
 * @program: SSM
 * @description:评论模块
 * @author: TLQ
 * @create: 2019-03-28 19:01
 **/
package com.tlq.service;

import com.tlq.entity.Comment;

import java.util.List;

public interface CommentService {
    /**
     *@Description: 添加评论
     *@Param: [comment]
     *@return: int
     *@Author: TLQ
     *@date: 2019/3/28 19:02
    **/
    public int add(Comment comment);

    /**
     *@Description: 更新评论
     *@Param: [comment]
     *@return: void
     *@Author: TLQ
     *@date: 2019/3/28 19:02
    **/
    public void update(Comment comment);

    /**
     *@Description: 根据文章id查询所有评论
     *@Param: [content_id]
     *@return: java.util.List<com.tlq.entity.Comment>
     *@Author: TLQ
     *@date: 2019/3/28 19:03
    **/
    public List<Comment> findAll(Long content_id);

    /**
     *@Description: 根据id查询评论
     *@Param: [id]
     *@return: com.tlq.entity.Comment
     *@Author: TLQ
     *@date: 2019/3/28 19:04
    **/
    public Comment findById(Long id);

    /**
     *@Description: 根据文章id查询所有父评论
     *@Param: [content_id]
     *@return: java.util.List<com.tlq.entity.Comment>
     *@Author: TLQ
     *@date: 2019/3/28 19:04
    **/
    public List<Comment> findAllFirstComment(Long content_id);

    /**
     *@Description: 根据文章id和子评论ids查询所有子评论
     *@Param: [content_id]
     *@return: java.util.List<com.tlq.entity.Comment>
     *@Author: TLQ
     *@date: 2019/3/28 19:05
    **/
    public List<Comment> findAllChildrenCommnet(Long content_id,String children);

    /**
     *@Description: 根据id删除评论
     *@Param: [id]
     *@return: void
     *@Author: TLQ
     *@date: 2019/3/28 19:06
    **/
    public  void deleteById(Long id);

    /**
     *@Description: 批量删除子评论
     *@Param: [children]
     *@return: void
     *@Author: TLQ
     *@date: 2019/3/28 19:07
    **/
    public void deleteChildrenComment(String children);


    /**
     *@Description: 根据文章id删除评论
     *@Param: [cid]
     *@return: void
     *@Author: TLQ
     *@date: 2019/3/28 19:07
    **/
    public void deleteByContentId(Long cid);
}