/**
 * @program: SSM
 * @description:
 * @author: TLQ
 * @create: 2019-03-28 19:39
 **/
package com.tlq.service.impl;

import com.tlq.dao.UserMapper;
import com.tlq.entity.User;
import com.tlq.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    private User user;

    @Transactional
    public int regist(User user) {
        return userMapper.insert(user);
    }

    public User login(String name, String password) {
        user.setEmail( name );
        user.setPassword( password );
        return userMapper.selectOne( user );
        //return userMapper.findUserByNameAndPwd( name,password );
    }

    public User findByEmail(String email) {
        user.setEmail( email );
        return userMapper.selectOne( user );
        // return userMapper.findByEmail(email);
    }

    @Override
    public User findByPhone(String phone) {
        user.setPhone(phone);
        return userMapper.selectOne(user);
    }

    @Override
    public User findById(Long id) {
        user.setId(id);
        return userMapper.selectOne(user);
    }

    public User findByEmailActive(String email) {
        User user = new User();
        user.setEmail( email );
        return userMapper.selectOne( user );
        // return userMapper.findByEmail(email);
    }

    public User findById(String id) {
        Long uid = Long.parseLong( id );
        user.setId( uid );
        return userMapper.selectOne( user );
    }

    public User findById(long id) {
        user.setId( id );
        return userMapper.selectOne( user );
    }
    @Transactional
    public void deleteByEmail(String email) {
        user.setEmail( email );
        userMapper.delete( user );
    }
    @Transactional
    public void deleteByEmailAndFalse(String email) {
        user.setEmail( email );
        userMapper.delete( user );
    }
    @Transactional
    public void update(User user) {
        userMapper.active(user);
    }
}