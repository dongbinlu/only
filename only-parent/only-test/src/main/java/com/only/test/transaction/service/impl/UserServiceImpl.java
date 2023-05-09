package com.only.test.transaction.service.impl;

import com.only.test.transaction.dao.RoleDao;
import com.only.test.transaction.dao.UserDao;
import com.only.test.transaction.entity.Role;
import com.only.test.transaction.entity.User;
import com.only.test.transaction.service.UserService;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = RuntimeException.class)
    public void insert(User user, Role role) {
        userDao.insert(user);
        //int x = 1/0;
        UserService proxy = (UserService) AopContext.currentProxy();
        proxy.insertRole(role);
        //this.insertRole(role);
        //int x = 1 / 0;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = RuntimeException.class)
    public void insertRole(Role role) {
        try {
            roleDao.insert(role);
            //int x = 1 / 0;
        } catch (Exception e) {
            throw new RuntimeException("除0异常");
        }
    }
}
