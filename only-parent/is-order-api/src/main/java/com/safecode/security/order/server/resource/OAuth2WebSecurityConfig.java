package com.safecode.security.order.server.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationManager;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

/*
 * 配置资源服务怎么校验令牌，控制认证
 */
@Configuration
@EnableWebSecurity
public class OAuth2WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    /*
     * 资源服务校验令牌的相关配置
     */
    @Bean
    public ResourceServerTokenServices tokenServices() {
        RemoteTokenServices tokenServices = new RemoteTokenServices();
        tokenServices.setClientId("order-service");
        tokenServices.setClientSecret("123456");
        tokenServices.setCheckTokenEndpointUrl("http://127.0.0.1:8030/oauth/check_token");

        //setAccessTokenConverter就是将令牌转换为用户信息
        tokenServices.setAccessTokenConverter(getAccessTokenConverter());
        return tokenServices;
    }

    //将令牌转换为用户信息
    private AccessTokenConverter getAccessTokenConverter() {

        DefaultAccessTokenConverter accessTokenConverter = new DefaultAccessTokenConverter();

        DefaultUserAuthenticationConverter userTokenConverter = new DefaultUserAuthenticationConverter();
        userTokenConverter.setUserDetailsService(userDetailsService);

        //setUserTokenConverter(),因为token里面只有用户名，在这里改成自己想要的用户信息
        accessTokenConverter.setUserTokenConverter(userTokenConverter);

        return accessTokenConverter;
    }

    /*
     * 因为要校验用户相关的信息，所以要覆盖authenticationManagerBean
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        OAuth2AuthenticationManager authenticationManager = new OAuth2AuthenticationManager();
        authenticationManager.setTokenServices(tokenServices());
        return authenticationManager;
    }
}
