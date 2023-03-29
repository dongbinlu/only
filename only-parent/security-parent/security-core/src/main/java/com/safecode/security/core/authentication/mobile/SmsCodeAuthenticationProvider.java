package com.safecode.security.core.authentication.mobile;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public class SmsCodeAuthenticationProvider implements AuthenticationProvider {

    private UserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        SmsCodeAuthenticationToken authenticationToken = (SmsCodeAuthenticationToken) authentication;

        //手机号登录者未必手机号注册过，所以这里通过查找数据库可能为空，导致无法获取用户信息
        //一下做法是手机号在系统中注册过，可以查出来用户信息，将用户信息放到UserDetails中
        UserDetails user = userDetailsService.loadUserByUsername((String) authenticationToken.getPrincipal());
        if (user == null) {
            throw new InternalAuthenticationServiceException("无法获取用户信息");
        }
        SmsCodeAuthenticationToken authenticationResult = new SmsCodeAuthenticationToken(user, user.getAuthorities());

        //还有一种情况是用户没有在系统中注册过，需要手机号，验证码即可登录，如下：
//		SmsCodeAuthenticationToken authenticationResult = new SmsCodeAuthenticationToken(authenticationToken.getPrincipal() , null);

        authenticationResult.setDetails(authenticationToken.getDetails());

        return authenticationResult;
    }

    //判断传进来的authentication是不是SmsCodeAuthentication类型的
    @Override
    public boolean supports(Class<?> authentication) {
        return SmsCodeAuthenticationToken.class.isAssignableFrom(authentication);
    }

    public UserDetailsService getUserDetailsService() {
        return userDetailsService;
    }

    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }


}
