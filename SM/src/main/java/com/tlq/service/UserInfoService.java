/**
 * @program: SSM
 * @description:用户信息
 * @author: TLQ
 * @create: 2019-03-28 19:36
 **/
package com.tlq.service;

import com.tlq.entity.UserInfo;

public interface UserInfoService {
    /**
     *@Description: 根据用户id查找用户详细信息
     *@Param: [id]
     *@return: com.tlq.entity.UserInfo
     *@Author: TLQ
     *@date: 2019/3/28 19:37
    **/
    public UserInfo findByUid(Long id);

    /**
     *@Description: 更新用户详细信息
     *@Param: [userInfo]
     *@return: void
     *@Author: TLQ
     *@date: 2019/3/28 19:38
    **/
    public void update(UserInfo userInfo);

    /**
     *@Description: 添加用户详细
     *@Param: [userInfo]
     *@return: void
     *@Author: TLQ
     *@date: 2019/3/28 19:38
    **/
    public void add(UserInfo userInfo);
}