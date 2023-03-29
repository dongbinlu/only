package com.safecode.security.core.validate.code;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author v_boy
 * 统一管理校验码处理器
 */
@Component
public class ValidateCodeProcessorHolder {

    //ImageCodeProcessor imageValidateCodeProcessor
    //SmsCodeProcessor smsValidateCodeProcessor
    @Autowired
    private Map<String, ValidateCodeProcessor> validateCodeProcessors;

    public ValidateCodeProcessor findValidateCodeProcessor(ValidateCodeType type) {
        return findValidateCodeProcessor(type.toString().toLowerCase());
    }

    public ValidateCodeProcessor findValidateCodeProcessor(String type) {
        //smsValidateCodeProcessor
        String name = type.toLowerCase() + ValidateCodeProcessor.class.getSimpleName();
        //SmsCodeProcessor
        ValidateCodeProcessor processor = validateCodeProcessors.get(name);
        if (processor == null) {
            throw new ValidateCodeException("验证码处理器" + name + "不存在");
        }
        return processor;
    }

}
