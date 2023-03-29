package com.safecode.security.price.auth;

import java.util.Date;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class UserInfo {

    private int user_id;

    private String user_name;

    private String nickname;

    private String[] aud;

    private String[] scope;

    private Date exp;

    private String[] authorities;

    private String jti;

    private String client_id;

}
