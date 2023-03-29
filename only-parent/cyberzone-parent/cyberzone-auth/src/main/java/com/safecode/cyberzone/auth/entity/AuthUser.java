package com.safecode.cyberzone.auth.entity;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class AuthUser extends User {

    // 用户ID
    private Integer userId;

    // 人脸权限
    private Integer facePerm;

    // 人脸路径
    private String faceId;

    private String nickName;

    private static final long serialVersionUID = 1L;

    public AuthUser(Integer userId, String username, String nickName, String password, Integer facePerm, String faceId,
                    boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked,
                    Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.userId = userId;
        this.facePerm = facePerm;
        this.faceId = faceId;
        this.nickName = nickName;

    }

    public AuthUser(Integer userId, String username, String nickName, String password, Integer facePerm, String faceId,
                    Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.userId = userId;
        this.facePerm = facePerm;
        this.faceId = faceId;
        this.nickName = nickName;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getFacePerm() {
        return facePerm;
    }

    public void setFacePerm(Integer facePerm) {
        this.facePerm = facePerm;
    }

    public String getFaceId() {
        return faceId;
    }

    public void setFaceId(String faceId) {
        this.faceId = faceId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }


}
