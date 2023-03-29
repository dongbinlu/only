package com.safecode.cyberzone.auth.exception;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safecode.cyberzone.base.dto.ResponseData;

/**
 * 无效token 未登录 异常重写
 *
 * @author v_boy
 */
@Component("authExceptionEntryPoint")
public class AuthExceptionEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        @SuppressWarnings("rawtypes")
        ResponseData data = new ResponseData();
        Throwable cause = authException.getCause();
        if (cause instanceof InvalidTokenException) {
            data.setCode(HttpStatus.UNAUTHORIZED.value());
            data.setMsg("无效的token");
        } else {
            data.setCode(HttpStatus.UNAUTHORIZED.value());
            data.setMsg("访问此资源需要完全的身份验证");
        }
        response.setContentType("application/json");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(response.getOutputStream(), data);
        } catch (Exception e) {
            throw new ServletException();
        }
    }

}
