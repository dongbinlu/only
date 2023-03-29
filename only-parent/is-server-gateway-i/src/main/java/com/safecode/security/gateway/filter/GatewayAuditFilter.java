package com.safecode.security.gateway.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.extern.slf4j.Slf4j;

/*
 * 日志处理
 */
@Slf4j
public class GatewayAuditFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("1, add log for {}", username);

        filterChain.doFilter(request, response);

        if (StringUtils.isBlank((String) request.getAttribute("logUpdated"))) {
            int status = response.getStatus();
            log.info("3, update log success");
        }


    }

}
