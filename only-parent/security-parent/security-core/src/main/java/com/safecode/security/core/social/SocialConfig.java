package com.safecode.security.core.social;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.social.security.SpringSocialConfigurer;

import com.safecode.security.core.properties.SecurityProperties;

@Order(10)
@Configuration
@EnableSocial
//社交配置的一个适配器
public class SocialConfig extends SocialConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired(required = false)
    private ConnectionSignUp registConnectionSignUp;

    /**
     * 创建表之后：表字段数据
     * userId 业务系统的用户id
     * providerId 服务提供商的id，是qq还是微信
     * providerUserId openid
     */

    //将connection的用户数据保存在数据库里，还需要JdbcUserConnectionRepossitory类
    //将业务系统的用户信息和服务提供商的用户信息关联起来
    @Override
    public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
        //第一个参数：数据源
        //第二个参数：getUsersConnectionRepository()方法会传进来，作用是检测ConnectionFactory，因为系统由多个ConnectionFactory工厂 例如微信的工厂
        //第三个参数：将插入数据库里面的数据进行加解密  在这里不做任何加解密
        JdbcUsersConnectionRepository repository = new JdbcUsersConnectionRepository(dataSource,
                connectionFactoryLocator, Encryptors.noOpText());

        //表名称的前缀
        repository.setTablePrefix("social_");

        //后台默认注册处理
        if (registConnectionSignUp != null) {
            repository.setConnectionSignUp(registConnectionSignUp);
        }

        return repository;

    }

    //将SocialAuthenticationFilter配置到springsecurity过滤连里
    @Bean("mySpringSocialConfigurer")
    public SpringSocialConfigurer mySpringSocialConfigurer() {
        String filterProcessesUrl = securityProperties.getSocial().getFilterProcessesUrl();

        //配置SocialAuthenticatioinFilter的auth路径
        MySpringSocialConfigurer configurer = new MySpringSocialConfigurer(filterProcessesUrl);

        //社交注册
        configurer.signupUrl(securityProperties.getBrowser().getSignUpUrl());
        return configurer;
    }

    /*
     * ProviderSignInUtils解决两个问题
     * 第一个：在注册过程中如何拿到springsocial的信息
     * 第二个：注册完成了如何把业务系统用户id再传给springsocial
     *
     */
    @Bean("providerSignInUtils")
    public ProviderSignInUtils providerSignInUtils(ConnectionFactoryLocator connectionFactoryLocator) {
        return new ProviderSignInUtils(connectionFactoryLocator,
                getUsersConnectionRepository(connectionFactoryLocator));
    }
}
