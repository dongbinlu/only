package com.safecode.security.core.social.qq.connect;

import org.springframework.social.oauth2.AbstractOAuth2ServiceProvider;

import com.safecode.security.core.social.qq.api.QQ;
import com.safecode.security.core.social.qq.api.QQImpl;

public class QQServiceProvider extends AbstractOAuth2ServiceProvider<QQ> {

    //需要自己处理 传进去即可
    private String appId;

    //将用户信息导向认证服务器
    private static final String URL_AUTHORIZE = "https://graph.qq.com/oauth2.0/authorize";

    //携带授权码申请令牌
    private static final String URL_ACCESS_TOKEN = "https://graph.qq.com/oauth2.0/token";


    //appId appSecret 在qq互联上注册的时候，会给出来
    //URL_AUTHORIZE 对应流程中的第一步：将用户信息导向认证服务器
    //URL_ACCESS_TOKEN 对应流程中的第四步：携带授权码申请令牌
    public QQServiceProvider(String appId, String appSecret) {

        //做1-5步 完成OAuth
        super(new QQOAuth2Template(appId, appSecret, URL_AUTHORIZE, URL_ACCESS_TOKEN));
        this.appId = appId;
    }

    @Override
    public QQ getApi(String accessToken) {
        return new QQImpl(accessToken, appId);
    }

}
