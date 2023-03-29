package com.safecode.security.core.validate.code;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.safecode.security.core.properties.SecurityProperties;
import com.safecode.security.core.validate.code.image.ImageCodeGenerator;
import com.safecode.security.core.validate.code.sms.DefaultSmsCodeSender;
import com.safecode.security.core.validate.code.sms.SmsCodeSender;

@Configuration
public class ValidateCodeBeanConfig {

    @Autowired
    private SecurityProperties securityProperties;

    @Bean
    //系统启动的时候，在初始化这个Bean之前，先在Spring容器中找，名字是imageValidateCodeGenerator这样的一个Bean,如果找到了就不会用下面的这个Bean了，就会用找的Bean
    @ConditionalOnMissingBean(name = "imageValidateCodeGenerator")
    public ValidateCodeGenerator imageValidateCodeGenerator() {
        ImageCodeGenerator codeGenerator = new ImageCodeGenerator();
        codeGenerator.setSecurityProperties(securityProperties);
        return codeGenerator;
    }

    //如果系统里找到了SmsCodeSender的实现，就不会用下面的了
    @Bean
    @ConditionalOnMissingBean(SmsCodeSender.class)
    public SmsCodeSender smsCodeSender() {
        return new DefaultSmsCodeSender();
    }

}
