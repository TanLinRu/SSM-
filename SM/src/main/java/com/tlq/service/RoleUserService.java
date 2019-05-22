/**
 * @program: SSM
 * @description:用户角色
 * @author: TLQ
 * @create: 2019-03-28 17:18
 **/
package com.tlq.service;

import com.tlq.entity.RoleUser;
import com.tlq.entity.User;

import java.util.List;

public interface RoleUserService {
    /**
     *@Description: 根据用户查询角色用户集合
     *@Param: [user]
     *@return: java.util.List<com.tlq.entity.RoleUser>
     *@Author: TLQ
     *@date: 2019/3/28 18:59
    **/
    public List<RoleUser> findByUser(User user);

    /**
     *@Description: 添加用户角色中间对象
     *@Param: [roleUser]
     *@return: int
     *@Author: TLQ
     *@date: 2019/3/28 19:00
    **/
    public int add(RoleUser roleUser);

    /**
     *@Description: 根据用户id删除
     *@Param: [uid]
     *@return: void
     *@Author: TLQ
     *@date: 2019/3/28 19:00
    **/
    public void deleteByUid(Long uid);
}