/**
 * @program: SSM
 * @description:角色模块
 * @author: TLQ
 * @create: 2019-03-28 17:11
 **/
package com.tlq.service;

import com.tlq.entity.Role;

import java.util.List;

public interface RoleService {
    /**
     *@Description: 根据id查询角色
     *@Param: [id]
     *@return: com.tlq.entity.Role
     *@Author: TLQ
     *@date: 2019/3/28 17:12
    **/
    public  List<Role> findById(Long id);

    /**
     *@Description: 添加角色
     *@Param: [role]
     *@return: int
     *@Author: TLQ
     *@date: 2019/3/28 17:14
    **/
    public int add(Role role);

    /**
     *@Description:根据用户id查询所有角色
     *@Param: [uid]
     *@return: java.util.List<com.tlq.entity.Role>
     *@Author: TLQ
     *@date: 2019/3/28 17:17
    **/
    public List<Role> findByUid(Long uid);
}