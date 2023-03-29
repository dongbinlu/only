package com.safecode.security.gateway.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.http.AccessTokenRequiredException;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/*
 * 401异常处理
 */
@Component
@Slf4j
public class GatewayAuthenticationEntryPoint extends OAuth2AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        if (authException instanceof AccessTokenRequiredException) {
            // 当前没传令牌
            log.info("2. udpate log to 401");
        } else {
            // 传的令牌有问题
            log.info("2. add log 401");
        }
        request.setAttribute("logUpdated", "yes");
        super.commence(request, response, authException);
    }

}
