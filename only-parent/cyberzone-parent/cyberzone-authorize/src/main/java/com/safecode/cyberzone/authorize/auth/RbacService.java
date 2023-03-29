package com.safecode.cyberzone.authorize.auth;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;

public interface RbacService {
    public boolean hasPermission(HttpServletRequest request, Authentication authentication);
}
