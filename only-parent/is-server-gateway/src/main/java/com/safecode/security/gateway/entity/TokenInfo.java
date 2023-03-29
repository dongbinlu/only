package com.safecode.security.gateway.entity;

import java.util.Date;

import lombok.Data;

@Data
public class TokenInfo {

    // 申明token是否可用
    private boolean active;

    // 这个令牌是发给哪个应用的
    private String client_id;

    private String[] scope;

    // 这个令牌是发给哪个用户的
    private String user_name;

    // 其实就是resourceId,这个令牌能访问哪个资源服务器
    private String[] aud;

    // 令牌过期时间
    private Date exp;

    // 这个令牌对于用户有哪些权限
    private String[] authorities;

}
