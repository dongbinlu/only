package com.only.rabbitMQ.producer.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan(basePackages = {"com.only.rabbitMQ.producer"})
@EnableTransactionManagement
@EnableAsync
@EnableAspectJAutoProxy
@EnableScheduling
@MapperScan(value = "com.only.rabbitMQ.producer.mapper")
public class MainConfig {
}
