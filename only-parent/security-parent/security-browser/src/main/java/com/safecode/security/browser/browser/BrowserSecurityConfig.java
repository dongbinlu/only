package com.safecode.security.browser.browser;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;
import org.springframework.social.security.SpringSocialConfigurer;

import com.safecode.security.browser.authentication.MySignOutSuccessHandler;
import com.safecode.security.core.authentication.AbstractChannelSecurityConfig;
import com.safecode.security.core.authentication.mobile.SmsCodeAuthenticationSecurityConfig;
import com.safecode.security.core.properties.SecurityConstants;
import com.safecode.security.core.properties.SecurityProperties;
import com.safecode.security.core.validate.code.ValidateCodeSecurityConfig;

@Configuration
// 专门用来做web应用安全的适配器
public class BrowserSecurityConfig extends AbstractChannelSecurityConfig {

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private DataSource dataSource;

    // 在记住我功能时，通过TokenRepository到数据库里查Token，将用户名获取处理后，要调用UserDetailsService去获取用户信息
    @Autowired
    private UserDetailsService myUserDetailsService;

    @Autowired
    private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;

    @Autowired
    private ValidateCodeSecurityConfig validateCodeSecurityConfig;

    @Autowired
    private SpringSocialConfigurer mySpringSocialConfigurer;

    @Autowired
    private SessionInformationExpiredStrategy sessionInformationExpiredStrategy;

    @Autowired
    private InvalidSessionStrategy invalidSessionStrategy;

    @Autowired
    private MySignOutSuccessHandler mySignOutSuccessHandler;

    // TokenRepository配置 记住我功能，将Token写入数据库里
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        // 初始创建表
        // tokenRepository.setCreateTableOnStartup(true);
        return tokenRepository;
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        applyPasswordAuthenticationConfig(http);

        http
                // 手机号登录
                .apply(smsCodeAuthenticationSecurityConfig)
                .and()
                // 验证码
                .apply(validateCodeSecurityConfig)
                .and()
                // springsocial登录
                .apply(mySpringSocialConfigurer)
                .and()
                // 记住我功能
                .rememberMe()
                // token存储
                .tokenRepository(persistentTokenRepository())
                // token过期时间
                .tokenValiditySeconds(securityProperties.getBrowser().getRememberMeSeconds())
                // 从token中获取用户名，自己重新登录
                .userDetailsService(myUserDetailsService)
                .and()
                // session失效
                .sessionManagement()
                //session失效处理方式
                .invalidSessionStrategy(invalidSessionStrategy)
                //session最大访问数量
                .maximumSessions(securityProperties.getBrowser().getSession().getMaximumSessions())
                //maxSessionPreventLogin 值为true时，阻止后续登录
                .maxSessionsPreventsLogin(securityProperties.getBrowser().getSession().isMaxSessionsPreventsLogin())
                .expiredSessionStrategy(sessionInformationExpiredStrategy)
                .and()
                .and()
                .logout()
                .logoutUrl("/signOut")
                // 退出成功后跳往一个指定页面，与logoutSuccessHandler互斥，只能配置一个
                //.logoutSuccessUrl("/signOut.html")
                //.and()
                .logoutSuccessHandler(mySignOutSuccessHandler)
                .deleteCookies("JSESSIONID")
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
