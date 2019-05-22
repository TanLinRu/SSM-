/**
 * @program: SSM
 * @description:点赞模块
 * @author: TLQ
 * @create: 2019-03-28 19:08
 **/
package com.tlq.service;

import com.tlq.entity.Upvote;

public interface UpvoteService {
    /**
     *@Description: 根据用户id和文章id查询
     *@Param: [upvote]
     *@return: com.tlq.entity.Upvote
     *@Author: TLQ
     *@date: 2019/3/28 19:09
    **/
    public Upvote findByUidAndConld(Upvote upvote);

    /**
     *@Description: 添加upvote
     *@Param: [upvote]
     *@return: int
     *@Author: TLQ
     *@date: 2019/3/28 19:12
    **/
    public int add(Upvote upvote);

   /**
    *@Description: 根据用户id查询最后一次登录的Upvote
    *@Param: [upvote]
    *@return: com.tlq.entity.Upvote
    *@Author: TLQ
    *@date: 2019/3/28 19:12
   **/
    public Upvote getByUid(Upvote upvote);

    /**
     *@Description: 更新Upvote
     *@Param: [upvote]
     *@return: void
     *@Author: TLQ
     *@date: 2019/3/28 19:13
    **/
    public void update(Upvote upvote);

    /**
     *@Description: 根据文章id删除Upvote
     *@Param: [cid]
     *@return: void
     *@Author: TLQ
     *@date: 2019/3/28 19:13
    **/
    public void deleteByContentId(Long cid);
}