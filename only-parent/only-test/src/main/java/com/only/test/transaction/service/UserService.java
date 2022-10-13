package com.only.test.transaction.service;

import com.only.test.transaction.entity.Role;
import com.only.test.transaction.entity.User;

public interface UserService {

    void insert(User user , Role role);

    void insertRole(Role role);
}
