/**
 * @program: SSM
 * @description:
 * @author: TLQ
 * @create: 2019-03-28 19:45
 **/
package com.tlq.service.impl;

import com.tlq.dao.UserInfoMapper;
import com.tlq.entity.UserInfo;
import com.tlq.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserInfoServiceImpl implements UserInfoService {
    @Autowired
    private UserInfoMapper userInfoMapper;

    @Override
    public UserInfo findByUid(Long id) {
        return null;
    }

    @Override
    public void update(UserInfo userInfo) {

    }

    @Override
    public void add(UserInfo userInfo) {

    }
}