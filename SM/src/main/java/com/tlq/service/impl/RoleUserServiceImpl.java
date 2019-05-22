/**
 * @program: SSM
 * @description:
 * @author: TLQ
 * @create: 2019-03-28 19:44
 **/
package com.tlq.service.impl;

import com.tlq.entity.RoleUser;
import com.tlq.entity.User;
import com.tlq.service.RoleUserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleUserServiceImpl implements RoleUserService {

    @Override
    public List<RoleUser> findByUser(User user) {
        return null;
    }

    @Override
    public int add(RoleUser roleUser) {
        return 0;
    }

    @Override
    public void deleteByUid(Long uid) {

    }
}