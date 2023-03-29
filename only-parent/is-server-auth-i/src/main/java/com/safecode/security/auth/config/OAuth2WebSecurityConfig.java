package com.safecode.security.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import com.safecode.security.auth.handler.OAuth2AuthenticationFailureHandler;
import com.safecode.security.auth.handler.OAuth2AuthenticationSuccessHandler;
import com.safecode.security.auth.properties.SecurityConstants;

/*
 * 在这个类里需要配置AuthenticationManager，让认证服务器知道去识别用户是否是有效的用户
 */
@Configuration
@EnableWebSecurity
public class OAuth2WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /*
     * UserDetailsService还没地方来
     */
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private LogoutSuccessHandler logoutSuccessHandler;

    @Autowired
    private OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

    @Autowired
    private OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    /*
     * 构建AuthenticationManager
     * 构建AuthenticationManager需要UserDetailsService和PasswordEncoder
     * 这里只是一个配置，怎样将AuthenticationManager配置出来，并没有将它放到spring容器中
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        //UserDetailsService从哪来，先别管，先注入进来,PasswordEncoder也注入进来
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
    }

    /*
     * 将AuthenticationManager暴露出去，交给spring容器
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().and()
                .authorizeRequests()
                .antMatchers(SecurityConstants.DEFAULT_LOGIN_PAGE_URL).permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginPage(SecurityConstants.DEFAULT_LOGIN_PAGE_URL)
                .loginProcessingUrl(SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_FORM)
                .successHandler(oAuth2AuthenticationSuccessHandler)
                .failureHandler(oAuth2AuthenticationFailureHandler)
                .and()
                .logout()
                .logoutSuccessHandler(logoutSuccessHandler)
                .deleteCookies("JSESSIONID")
                .and()
                // 跨站请求的伪造防护
                .csrf().disable();
    }

}
