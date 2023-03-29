package com.safecode.security.auth.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Component
public class OAuth2LogoutHandler implements LogoutHandler {

    @Autowired
    private DataSource dataSource;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
//		退出时清除token
//		String authHeader = request.getHeader("Authorization");
//		String token = StringUtils.substringAfter(authHeader, "Bearer ");


        new JdbcTokenStore(dataSource).removeAccessToken("");
    }
}
