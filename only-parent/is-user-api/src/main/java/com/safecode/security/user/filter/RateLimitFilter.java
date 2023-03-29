package com.safecode.security.user.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.util.concurrent.RateLimiter;

/**
 * <p>@Title: RateLimitFilter.java<p>
 * <p>@Description: OncePerRequestFilter保证过滤器里的逻辑在一个请求里永远只被执行一次
 * 任何实现Filter接口的类，SpringBoot会自动把它加载到web应用的过滤器链上
 * </p>
 *
 * @author ludongbin
 * @date 2019年10月18日下午1:21:47
 */

@Component
@Order(1)
public class RateLimitFilter extends OncePerRequestFilter {

    @Autowired
    private ObjectMapper objectMapper;

    // 1秒钟只能有1个请求通过
    private RateLimiter rateLimiter = RateLimiter.create(1);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        /*
         * 如果为True ，表示没有到达限流标准
         */
        if (rateLimiter.tryAcquire()) {
            filterChain.doFilter(request, response);
            return;
        }
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        response.getWriter().write(objectMapper.writeValueAsString("too many request !!!"));
        response.getWriter().flush();
        return;
    }

}
