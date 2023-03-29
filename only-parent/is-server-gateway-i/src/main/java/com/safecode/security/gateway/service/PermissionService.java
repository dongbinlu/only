package com.safecode.security.gateway.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;

public interface PermissionService {

    boolean hasPermission(HttpServletRequest request, Authentication authentication);

}
