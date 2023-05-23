package com.only.order.center.config;

import com.only.order.center.interceptor.CustomRequestInterceptor;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;

public class FeignConfig {

    @Bean
    public RequestInterceptor requestInterceptor(){
        return new CustomRequestInterceptor();
    }
}
