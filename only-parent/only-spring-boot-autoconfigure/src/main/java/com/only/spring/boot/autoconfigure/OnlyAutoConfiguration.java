package com.only.spring.boot.autoconfigure;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;


/**
 * 需求：
 * 当配置开关开启时生效，未开启时不生效
 * 从容器中获取OnlySessionTemplate,用来执行sql
 */

@Configuration
@EnableConfigurationProperties({OnlyProperties.class})

// 只有在only.session.flag配置为true时生效，默认不生效
@ConditionalOnProperty(prefix = "only.session", name = "flag", havingValue = "true", matchIfMissing = false)
public class OnlyAutoConfiguration implements InitializingBean {

    private final OnlyProperties onlyProperties;

    public OnlyAutoConfiguration(OnlyProperties onlyProperties) {
        this.onlyProperties = onlyProperties;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.checkConfig();
    }

    private void checkConfig() {
        if (StringUtils.isEmpty(onlyProperties.getUrl())) {
            throw new IllegalArgumentException("the url is not allowed to be empty.");
        }
    }

    @Bean
    @ConditionalOnMissingBean
    public OnlySessionTemplate onlySessionTemplate() {
        return new OnlySessionTemplate();
    }


}
