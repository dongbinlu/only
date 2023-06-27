package com.only.mvc.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;


/**
 * IOC根容器，不扫描Controller的注解
 */
@Configuration
@ComponentScan(basePackages = "com.only.mvc.controller", excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ANNOTATION, value = {RestController.class, Controller.class}),
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = WebAppConfig.class)
})
public class RootConfig {
}
