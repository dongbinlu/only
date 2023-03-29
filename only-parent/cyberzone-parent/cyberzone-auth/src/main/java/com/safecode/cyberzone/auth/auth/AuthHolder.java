package com.safecode.cyberzone.auth.auth;

import java.util.Collection;
import java.util.LinkedHashMap;

import org.springframework.beans.BeanUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

import com.safecode.cyberzone.auth.entity.AuthUser2;

public class AuthHolder {

    public static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static OAuth2Authentication getAuth2Authentication() {
        return (OAuth2Authentication) getAuthentication();
    }

    public static Authentication getUserAuthentication() {
        return getAuth2Authentication().getUserAuthentication();
    }

    public static Collection<? extends GrantedAuthority> getAuthorities() {
        return getUserAuthentication().getAuthorities();
    }

    @SuppressWarnings("unchecked")
    public static Integer getCurrentUserId() {
        Authentication authentication = getUserAuthentication();
        Object obj = authentication.getPrincipal();
        AuthUser2 authUser = new AuthUser2(null, null);
        BeanUtils.copyProperties(obj, authUser);
        return authUser.getUserId();
    }

    @SuppressWarnings("unchecked")
    public static String getCurrentUsername() {
        LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) getUserAuthentication().getDetails();
        return (String) map.get("username");
    }

    public static String getRemoteIp() {
        Object details = getAuthentication().getDetails();
        String remoteIp = details.toString().split(",")[0].split("=")[1];
        return remoteIp;
    }

    public static OAuth2AuthenticationDetails getOAuth2AuthenticationDetails() {
        OAuth2AuthenticationDetails oAuth2AuthenticationDetails = (OAuth2AuthenticationDetails) getAuth2Authentication()
                .getDetails();
        return oAuth2AuthenticationDetails;
    }

    public static String getCurrentToken() {
        return getOAuth2AuthenticationDetails().getTokenValue();
    }
}
