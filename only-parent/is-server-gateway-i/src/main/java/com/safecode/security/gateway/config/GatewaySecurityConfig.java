package com.safecode.security.gateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;

import com.safecode.security.gateway.filter.GatewayAuditFilter;
import com.safecode.security.gateway.filter.GatewayRateLimitFilter;
import com.safecode.security.gateway.handler.GatewayAccessDeniedHandler;
import com.safecode.security.gateway.handler.GatewayAuthenticationEntryPoint;
import com.safecode.security.gateway.handler.GatewayWebSecurityExpressionHandler;

@Configuration
@EnableResourceServer
public class GatewaySecurityConfig extends ResourceServerConfigurerAdapter {

    @Autowired
    private GatewayWebSecurityExpressionHandler gateWebSecurityExpressionHandler;

    @Autowired
    private GatewayAccessDeniedHandler gatewayAccessDeniedHandler;

    @Autowired
    private GatewayAuthenticationEntryPoint gatewayAuthenticationEntryPoint;


    /*
     * 可以在这里不用配置resourceId,直接将数据表oauth_client_details的resource_ids的值清空
     */
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId("gateway")
                //表达式处理器
                .expressionHandler(gateWebSecurityExpressionHandler)
                //403处理器
                .accessDeniedHandler(gatewayAccessDeniedHandler)
                //401处理器
                .authenticationEntryPoint(gatewayAuthenticationEntryPoint);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                //SecurityContextPersistenceFilter为spring security 过滤器链的第一个过滤器
                .addFilterBefore(new GatewayRateLimitFilter(), SecurityContextPersistenceFilter.class)
                .addFilterBefore(new GatewayAuditFilter(), ExceptionTranslationFilter.class)
                .authorizeRequests()
                //将获取令牌的请求放行
                .antMatchers("/auth/**").permitAll()
                .anyRequest()
                //自己制定访问规则
                .access("#permissionService.hasPermission(request , authentication)");
    }

}
