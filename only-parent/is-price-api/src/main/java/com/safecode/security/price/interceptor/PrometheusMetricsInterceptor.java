package com.safecode.security.price.interceptor;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import io.prometheus.client.Counter;
import io.prometheus.client.Summary;

//给自定义Metrics监控指标赋值
@Component
public class PrometheusMetricsInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private Counter requestCounter;

    @Autowired
    private Summary requestLatency;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        request.setAttribute("startTime", new Date().getTime());
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        requestCounter.labels(request.getRequestURI(), request.getMethod(), String.valueOf(response.getStatus()))
                .inc();

        long duration = new Date().getTime() - (long) request.getAttribute("startTime");

        requestLatency.labels(request.getRequestURI(), request.getMethod(), String.valueOf(response.getStatus()))
                .observe(duration);
    }

}
