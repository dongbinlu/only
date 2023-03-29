package com.safecode.security.gateway.service.impl;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.http.AccessTokenRequiredException;
import org.springframework.stereotype.Service;

import com.safecode.security.gateway.service.PermissionService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PermissionServiceImpl implements PermissionService {

    @Override
    public boolean hasPermission(HttpServletRequest request, Authentication authentication) {
        log.info("request_uri:{}", request.getRequestURI());

        log.info("authentication:{}",
                ReflectionToStringBuilder.toString(authentication, ToStringStyle.MULTI_LINE_STYLE));

        // 校验是否登录，处理匿名用户
        if (authentication instanceof AnonymousAuthenticationToken) {
            throw new AccessTokenRequiredException(null);
        }

        // return RandomUtils.nextInt() % 2 == 0;
        return true;
    }

}
