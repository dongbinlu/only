package com.safecode.security.permission.common;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.safecode.security.permission.utils.JsonMapper;

import lombok.extern.slf4j.Slf4j;

@Component("httpInterceptor")
@Slf4j
public class HttpInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String uri = request.getRequestURI().toString();
        Map parameterMap = request.getParameterMap();
        log.info("请求开始：.uri:{} , params:{}", uri, JsonMapper.obj2String(parameterMap));
        long start = System.currentTimeMillis();
        request.setAttribute("time", start);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        String uri = request.getRequestURI().toString();
        long start = (long) request.getAttribute("time");
        long end = System.currentTimeMillis();
        log.info("请求完成：.uri{} , cost:{}", uri, end - start);
        removeThreadLocalInfo();
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        String uri = request.getRequestURI().toString();
        long start = (long) request.getAttribute("time");
        long end = System.currentTimeMillis();
        log.info("请求结束：.uri:{} , cost:{}", uri, (end - start));
        removeThreadLocalInfo();
    }

    public void removeThreadLocalInfo() {
        RequestHolder.remove();
    }

}
