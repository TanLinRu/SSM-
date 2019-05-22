/**
 * @program: SSM
 * @description:
 * @author: TLQ
 * @create: 2019-03-28 19:43
 **/
package com.tlq.service.impl;

import com.tlq.dao.RoleMapper;
import com.tlq.entity.Role;
import com.tlq.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleMapper roleMapper;

    public List<Role> findById(Long id) {
        return roleMapper.findByUid(id);
    }

    @Override
    public int add(Role role) {
        return roleMapper.insert(role);
    }

    public List<Role> findByUid(Long uid){
        List list = new LinkedList();
        return list;
    }


}