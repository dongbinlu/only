package com.safecode.security.user.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lambdaworks.crypto.SCryptUtil;
import com.safecode.security.user.dao.UserRepository;
import com.safecode.security.user.entity.User;
import com.safecode.security.user.entity.UserInfo;
import com.safecode.security.user.service.UserService;

@Service("userService")
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    public UserInfo create(UserInfo info) {
        User before = new User();
        BeanUtils.copyProperties(info, before);

        before.setPassword(SCryptUtil.scrypt(info.getPassword(), 32768, 8, 1));

        User after = userRepository.save(before);
        info.setId(after.getId());
        return info;
    }

    public UserInfo update(UserInfo user) {
        // TODO Auto-generated method stub
        return null;
    }

    public void delete(Long id) {
        // TODO Auto-generated method stub

    }

    public UserInfo get(Long id) {
        return userRepository.findById(id).get().buildInfo();
    }

    public List<UserInfo> query(UserInfo user) {
        return null;
    }

    public UserInfo login(UserInfo info) {
        UserInfo result = null;
        User user = userRepository.findByUsername(info.getUsername());
        if (user != null && SCryptUtil.check(info.getPassword(), user.getPassword())) {
            result = user.buildInfo();
        }
        return result;
    }

}
