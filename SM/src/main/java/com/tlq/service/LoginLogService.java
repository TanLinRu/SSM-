/**
 * @program: SSM
 * @description: 登录日志
 * @author: TLQ
 * @create: 2019-03-28 16:22
 **/
package com.tlq.service;
import  com.tlq.entity.LoginLog;

import java.util.List;


public interface LoginLogService {
    /**
     *@Description: 添加登录日志
     *@Param: [loginLog]
     *@return: int
     *@Author: TLQ
     *@date: 2019/3/28 16:53
    **/
    public int add(LoginLog loginLog);

    /**
     *@Description:
     *@Param: []
     *@return: java.util.List<com.tlq.entity.LoginLog>
     *@Author: TLQ
     *@date: 2019/3/28 16:54
    **/
    public List<LoginLog> findAll();

    /**
     *@Description:
     *@Param: [uid]
     *@return: java.util.List<com.tlq.entity.LoginLog>
     *@Author: TLQ
     *@date: 2019/3/28 16:57
    **/
    public List<LoginLog> findByUid(Long uid);
}