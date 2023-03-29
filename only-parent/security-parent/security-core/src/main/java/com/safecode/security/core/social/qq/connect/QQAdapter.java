package com.safecode.security.core.social.qq.connect;

import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;

import com.safecode.security.core.social.qq.api.QQ;
import com.safecode.security.core.social.qq.api.QQUserInfo;

public class QQAdapter implements ApiAdapter<QQ> {

    //测试当前的api是否可用
    @Override
    public boolean test(QQ api) {
        return true;
    }

    //在connection和api直接做一个适配
    @Override
    public void setConnectionValues(QQ api, ConnectionValues values) {
        //获取qq用户信息
        QQUserInfo userInfo = api.getUserInfo();
        //昵称 用户名
        values.setDisplayName(userInfo.getNickname());
        //用户头像
        values.setImageUrl(userInfo.getFigureurl_qq_1());
        //个人主页  在qq这儿只没用的
        values.setProfileUrl(null);
        //服务商的id 也就是openid
        values.setProviderUserId(userInfo.getOpenId());
    }

    @Override
    public UserProfile fetchUserProfile(QQ api) {
        return null;
    }

    @Override
    public void updateStatus(QQ api, String message) {

    }

}
