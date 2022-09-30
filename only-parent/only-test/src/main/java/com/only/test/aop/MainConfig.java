package com.only.test.aop;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
//exposeProxy = true 意思为暴露代理对象
@EnableAspectJAutoProxy(exposeProxy = true)
public class MainConfig {

    @Bean
    public Calculate calculate() {
        return new OnlyCalculate();
    }

    @Bean
    public OnlyLogAspect onlyLogAspect() {
        return new OnlyLogAspect();
    }
}