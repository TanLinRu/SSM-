/**
 * @program: SSM
 * @description:
 * @author: TLQ
 * @create: 2019-03-28 19:41
 **/
package com.tlq.service.impl;

import com.tlq.dao.CommentMapper;
import com.tlq.entity.Comment;
import com.tlq.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentMapper commentMapper;

    @Override
    public int add(Comment comment) {
        return commentMapper.insertComment(comment);
    }

    @Override
    public void update(Comment comment) {

    }

    @Override
    public List<Comment> findAll(Long content_id) {
        return commentMapper.selectAll(content_id);
    }

    @Override
    public Comment findById(Long id) {
        return null;
    }

    @Override
    public List<Comment> findAllFirstComment(Long content_id) {
        return commentMapper.findAllFirstComment(content_id);
    }

    @Override
    public List<Comment> findAllChildrenCommnet(Long content_id,String children) {
        return commentMapper.findAllChildrenComment(content_id,children);
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public void deleteChildrenComment(String children) {

    }

    @Override
    public void deleteByContentId(Long cid) {

    }
}