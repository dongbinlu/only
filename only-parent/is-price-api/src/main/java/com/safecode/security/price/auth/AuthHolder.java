package com.safecode.security.price.auth;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

public class AuthHolder {
    private static ObjectMapper objectMapper = new ObjectMapper();

    public static UserInfo getUserInfo() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        OAuth2AuthenticationDetails oAuth2AuthenticationDetails = (OAuth2AuthenticationDetails) auth.getDetails();
        String token = oAuth2AuthenticationDetails.getTokenValue();
        Jwt jwt = JwtHelper.decode(token);
        String claims = jwt.getClaims();
        UserInfo userInfo = null;
        try {
            userInfo = objectMapper.readValue(claims, UserInfo.class);
        } catch (Exception e) {

        }
        return userInfo;
    }
}
