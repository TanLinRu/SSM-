/**
 * @program: SSM
 * @description:
 * @author: TLQ
 * @create: 2019-03-30 15:21
 **/
package com.tlq.dao;

import com.tlq.entity.Comment;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface CommentMapper extends Mapper<Comment> {
    //根据文章id查询所有评论
    public List<Comment> selectAll(@Param("cid")long cid);
    //根据文章id查询所有一级评论
    public List<Comment> findAllFirstComment(@Param("cid")long cid);
    //根据文章id和二级评论ids查询出所有二级评论
    public List<Comment> findAllChildrenComment(@Param("cid")long cid,@Param("children")String children);
    //插入评论并返回主键id 返回值是影响行数  id在Comment对象中
    public int insertComment(Comment comment);
}