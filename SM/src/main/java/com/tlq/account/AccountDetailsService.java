/**
 * @program: SSM
 * @description:
 * @author: TLQ
 * @create: 2019-05-18 16:05
 **/
package com.tlq.account;

import com.tlq.entity.Role;
import com.tlq.entity.User;
import com.tlq.service.RoleService;
import com.tlq.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public class AccountDetailsService implements UserDetailsService {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = new User();
        if (user == null) {
            throw  new  UsernameNotFoundException("用户名或密码错误！");
        }
        List<Role> roles = roleService.findById(user.getId());
        user.setRoles(roles);
        return  user;
    }
}