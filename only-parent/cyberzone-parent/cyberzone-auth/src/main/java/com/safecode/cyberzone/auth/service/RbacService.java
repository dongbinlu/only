package com.safecode.cyberzone.auth.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;

public interface RbacService {
    public boolean hasPermission(HttpServletRequest request, Authentication authentication);
}
