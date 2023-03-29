package com.safecode.security.gateway.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import com.google.common.util.concurrent.RateLimiter;

import lombok.extern.slf4j.Slf4j;

/*
 * 加在SecurityContextPersistenceFilter之前，SecurityContextPersistenceFilter为spring security过滤器链的第一个过滤器
 * 先限流，后认证
 */
@Slf4j
public class GatewayRateLimitFilter extends OncePerRequestFilter {

    private RateLimiter rateLimiter = RateLimiter.create(100);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        log.info("0 rate limit");

        if (rateLimiter.tryAcquire()) {
            filterChain.doFilter(request, response);
            return;
        } else {
            response.setContentType("application/json;charset-UTF-8");
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.getWriter().write("{\"error\":\"too many requests\"}");
            response.getWriter().flush();
            return;
        }

    }

}
