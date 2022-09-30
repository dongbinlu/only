package com.only.multids.busi.service;

import com.only.multids.busi.bean.User;
import com.only.multids.busi.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl {

    @Autowired
    private UserMapper userMapper;

    public void insert(User user) {
        userMapper.insertUser(user);
    }


}
