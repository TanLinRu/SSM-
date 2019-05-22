/**
 * @program: SSM
 * @description:
 * @author: TLQ
 * @create: 2019-03-28 19:44
 **/
package com.tlq.service.impl;

import com.tlq.entity.Upvote;
import com.tlq.service.UpvoteService;
import org.springframework.stereotype.Service;

@Service
public class UpvoteServiceImpl implements UpvoteService {
//    @Autowired
//    private UpvoteMapper upvoteMapper;

    @Override
    public Upvote findByUidAndConld(Upvote upvote) {
        return null;
    }

    @Override
    public int add(Upvote upvote) {
        return 0;
    }

    @Override
    public Upvote getByUid(Upvote upvote) {
        return null;
    }

    @Override
    public void update(Upvote upvote) {

    }

    @Override
    public void deleteByContentId(Long cid) {

    }
}