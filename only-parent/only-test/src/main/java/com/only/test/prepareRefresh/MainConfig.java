package com.only.test.prepareRefresh;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
//exposeProxy = true 意思为暴露代理对象
@EnableAspectJAutoProxy(exposeProxy = true)
@ComponentScan(value = {"com.only.test.prepareRefresh"})
public class MainConfig {


}