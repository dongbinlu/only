package com.safecode.security.user.config;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.safecode.security.user.entity.UserInfo;
import com.safecode.security.user.interceptor.AclInterceptor;
import com.safecode.security.user.interceptor.AuditLogInterceptor;

@Configuration
@EnableJpaAuditing
public class SecurityConfig implements WebMvcConfigurer {

    @Autowired
    private AuditLogInterceptor auditLogInterceptor;

    @Autowired
    private AclInterceptor aclInterceptor;

    public void addInterceptors(InterceptorRegistry registry) {
        // 拦截器也有多个 ， 先添加的先执行
        registry.addInterceptor(auditLogInterceptor);

        registry.addInterceptor(aclInterceptor);
    }

    // 处理审计日志的用户
    @Bean
    public AuditorAware<String> auditorAware() {
        return new AuditorAware<String>() {

            public Optional<String> getCurrentAuditor() {
                // spring 的静态方法，获取session的值
                ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder
                        .getRequestAttributes();
                UserInfo user = (UserInfo) servletRequestAttributes.getRequest().getSession().getAttribute("user");
                String username = null;
                if (user != null) {
                    username = user.getUsername();
                }
                return Optional.ofNullable(username);
            }
        };
    }
}
