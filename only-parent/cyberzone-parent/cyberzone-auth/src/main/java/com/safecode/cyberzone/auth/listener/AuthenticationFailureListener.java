package com.safecode.cyberzone.auth.listener;

import java.io.IOException;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.safecode.cyberzone.auth.entity.SysLog;
import com.safecode.cyberzone.auth.service.SysLogService;
import com.safecode.cyberzone.auth.service.impl.LoginAttemptService;
import com.safecode.cyberzone.auth.utils.IpUtil;

@Component
public class AuthenticationFailureListener extends OncePerRequestFilter implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    @Autowired
    private LoginAttemptService loginAttemptService;
    @Autowired
    private SysLogService sysLogService;

    @Autowired
    private HttpServletRequest request;


    @Override
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
        if (!"access-token".equals(event.getAuthentication().getName())) {
            sysLogService.save(SysLog.builder().remark(event.getAuthentication().getName() + "登录失败")
                    .projectName("cyberzone-auth").requestObject("").requestUrl("/oauth/token").createTime(new Date())
                    .userName(event.getAuthentication().getName()).ip(IpUtil.getRemoteIp(request)).logStatus("登录日志").build());
        }
        loginAttemptService.loginFailed(event.getAuthentication().getName());
    }


    @Override
    protected void doFilterInternal(HttpServletRequest arg0, HttpServletResponse arg1, FilterChain arg2)
            throws ServletException, IOException {
        arg2.doFilter(arg0, arg1);
    }

}
