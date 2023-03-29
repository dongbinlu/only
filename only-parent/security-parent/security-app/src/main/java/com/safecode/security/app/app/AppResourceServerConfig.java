package com.safecode.security.app.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.social.security.SpringSocialConfigurer;

import com.safecode.security.core.authentication.mobile.SmsCodeAuthenticationSecurityConfig;
import com.safecode.security.core.properties.SecurityConstants;
import com.safecode.security.core.properties.SecurityProperties;
import com.safecode.security.core.validate.code.ValidateCodeSecurityConfig;

@Configuration
@EnableResourceServer
public class AppResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Autowired
    protected AuthenticationSuccessHandler myAuthenticationSuccessHandler;

    @Autowired
    protected AuthenticationFailureHandler myAuthenticationFailureHandler;

    @Autowired
    private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;

    @Autowired
    private ValidateCodeSecurityConfig validateCodeSecurityConfig;

    @Autowired
    private SpringSocialConfigurer mySpringSocialConfigurer;

    @Autowired
    private SecurityProperties securityProperties;

    @Override
    public void configure(HttpSecurity http) throws Exception {

        http.formLogin()
                .loginPage(SecurityConstants.DEFAULT_UNAUTHENTICATION_URL)
                .loginProcessingUrl(SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_FORM)
                .successHandler(myAuthenticationSuccessHandler)
                .failureHandler(myAuthenticationFailureHandler);

        // 手机号登录
        http.apply(smsCodeAuthenticationSecurityConfig)
                .and()
                .apply(validateCodeSecurityConfig)
                .and()
                // 将springsocial加入到过滤器链里面
                .apply(mySpringSocialConfigurer)
                .and()
                .authorizeRequests()
                // 当访问此url时，不需要身份认证
                .antMatchers(
                        // 当需要身份认证时跳转此Controller
                        SecurityConstants.DEFAULT_UNAUTHENTICATION_URL,
                        SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_MOBILE,
                        // 登录页
                        securityProperties.getBrowser().getLoginPage(),
                        // 验证码
                        SecurityConstants.DEFAULT_VALIDATE_CODE_URL_PREFIX + "/*",
                        // 社交注册页
                        securityProperties.getBrowser().getSignUpUrl(),
                        // 社交注册接口
                        "/regist",
                        // session失效
                        SecurityConstants.DEFAULT_SESSION_INVALID_URL,
                        // 退出成功后的页面
                        securityProperties.getBrowser().getSignOutUrl())
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                // 跨站请求的伪造防护
                .csrf().disable();
    }

}
