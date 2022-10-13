package com.only.test.transaction.service.impl;

import com.only.test.transaction.dao.RoleDao;
import com.only.test.transaction.entity.Role;
import com.only.test.transaction.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDao roleDao;

    @Override
    @Transactional
    public void insert(Role role) {
        roleDao.insert(role);
    }
}
