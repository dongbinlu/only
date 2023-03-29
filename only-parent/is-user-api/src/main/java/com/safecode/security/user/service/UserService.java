package com.safecode.security.user.service;

import java.util.List;

import com.safecode.security.user.entity.UserInfo;

/**
 * <p>@Title: UserService.java<p>
 * <p>@Description: 接口的方法都是共有抽象的</p>
 *
 * @author ludongbin
 * @date 2019年10月18日下午2:24:05
 */
public interface UserService {

    UserInfo create(UserInfo user);

    UserInfo update(UserInfo user);

    void delete(Long id);

    UserInfo get(Long id);

    List<UserInfo> query(UserInfo user);

    UserInfo login(UserInfo info);
}
