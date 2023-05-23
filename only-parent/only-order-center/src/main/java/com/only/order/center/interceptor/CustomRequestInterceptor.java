package com.only.order.center.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;

import java.util.UUID;

public class CustomRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header("token", UUID.randomUUID().toString());
    }
}
