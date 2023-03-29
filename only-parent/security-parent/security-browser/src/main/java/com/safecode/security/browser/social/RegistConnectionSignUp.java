package com.safecode.security.browser.social;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.stereotype.Component;

//qq第一次登录时，后台默认创建用户
@Component("registConnectionSignUp")
public class RegistConnectionSignUp implements ConnectionSignUp {

    @Override
    public String execute(Connection<?> connection) {
        // 根据社交用户信息默认创建用户并返回用户唯一标识
        return connection.getDisplayName();
    }

}
