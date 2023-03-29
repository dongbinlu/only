package com.safecode.security.core.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.safecode.security.core.properties.SecurityProperties;

@Configuration
@EnableConfigurationProperties(SecurityProperties.class)
// 这个类的作用会让SecurityProperties这个读取器生效
public class SecurityCoreConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        // 可以在这里实现PasswordEncoder接口配置自己的密码处理方式
        return new BCryptPasswordEncoder();
    }

}
