package com.safecode.security.order.server.resource;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

/*
 * 配置资源服务器
 */
@Configuration
@EnableResourceServer
public class OAuth2ResourceServerConfig extends ResourceServerConfigurerAdapter {

    /*
     * 配置资源服务器
     */
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId("order-server");
    }

    /*
     * 配置资源服务器权限相关的信息
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                //权限控制，根据scope进行权限控制，如果是POST请求，只能有写的权限
                .antMatchers(HttpMethod.POST).access("#oauth2.hasScope('write')")
                //如果是GET请求，只能有读的权限
                .antMatchers(HttpMethod.GET).access("#oauth2.hasScope('read')")
                .anyRequest()
                .authenticated();
    }

}
