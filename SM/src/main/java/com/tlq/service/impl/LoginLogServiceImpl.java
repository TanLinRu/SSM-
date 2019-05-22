/**
 * @program: SSM
 * @description:
 * @author: TLQ
 * @create: 2019-03-28 19:42
 **/
package com.tlq.service.impl;

import com.tlq.entity.LoginLog;
import com.tlq.service.LoginLogService;

import java.util.List;

public class LoginLogServiceImpl implements LoginLogService {
//    @Autowired
//    private LoginLogMapper loginLogMapper;

    @Override
    public int add(LoginLog loginLog) {
        return 0;
    }

    @Override
    public List<LoginLog> findAll() {
        return null;
    }

    @Override
    public List<LoginLog> findByUid(Long uid) {
        return null;
    }
}