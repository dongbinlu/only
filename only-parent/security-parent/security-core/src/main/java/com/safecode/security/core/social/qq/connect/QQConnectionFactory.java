package com.safecode.security.core.social.qq.connect;

import org.springframework.social.connect.support.OAuth2ConnectionFactory;

import com.safecode.security.core.social.qq.api.QQ;

public class QQConnectionFactory extends OAuth2ConnectionFactory<QQ> {

    public QQConnectionFactory(String providerId, String appId, String appSecret) {
        //第一个参数：提供商的唯一标识，通过配置文件配置进来
        //第二个参数：ServiceProvider
        //第三个参数：Adapter适配器
        super(providerId, new QQServiceProvider(appId, appSecret), new QQAdapter());
    }

}
