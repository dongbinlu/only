package com.safecode.security.order.service.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.safecode.security.order.entity.User;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 正式环境需要查询数据库
        User user = new User();
        user.setId(1L);
        user.setUsername(username);
        return user;
    }

}
