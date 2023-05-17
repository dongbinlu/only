package com.only.test.mybatis.service;

import com.only.test.mybatis.entity.User;
import com.only.test.mybatis.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User getUser(Integer userId) {

        userMapper.getByUserId(userId);

        return userMapper.getByUserId(userId);
    }

    @Override
    @Transactional()
    public User getUser2(Integer userId) {
        userMapper.getByUserId(userId);
        User user = userMapper.getByUserId(userId);
        return user;
    }

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public User getUser3(Integer userId) {
        userMapper.getByUserId(userId);
        return userMapper.getByUserId(userId);
    }
}
