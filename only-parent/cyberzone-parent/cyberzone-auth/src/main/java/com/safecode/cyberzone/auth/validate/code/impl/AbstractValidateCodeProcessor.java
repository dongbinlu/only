package com.safecode.cyberzone.auth.validate.code.impl;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;

import com.safecode.cyberzone.auth.validate.code.ValidateCode;
import com.safecode.cyberzone.auth.validate.code.ValidateCodeException;
import com.safecode.cyberzone.auth.validate.code.ValidateCodeGenerator;
import com.safecode.cyberzone.auth.validate.code.ValidateCodeProcessor;
import com.safecode.cyberzone.auth.validate.code.ValidateCodeRepository;
import com.safecode.cyberzone.auth.validate.code.ValidateCodeType;

public abstract class AbstractValidateCodeProcessor<C extends ValidateCode> implements ValidateCodeProcessor {

    /**
     * 操作session的工具类
     */

    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    /**
     * 收集系统中所有{@link ValidateCodeGenerator} 接口的实现。 key为在spring容器中Bean的名字
     */

    // ValidateCodeGenerator ImageValidateCodeGenerator
    @Autowired
    private Map<String, ValidateCodeGenerator> validateCodeGenerators;

    @Autowired
    private ValidateCodeRepository validateCodeRepository;

    /**
     * 创建校验码
     *
     * @param request
     * @throws Exception
     */

    @Override
    public void create(ServletWebRequest request) throws Exception {
        //ImageCode
        C validateCode = generate(request);
        save(request, validateCode);
        send(request, validateCode);

    }

    /**
     * 生成校验码
     *
     * @param request
     * @return
     */
    private C generate(ServletWebRequest request) {
        //image
        String type = getValidateCodeType(request).toString().toLowerCase();
        //image + ValidateCodeGenerator
        String generatorName = type + ValidateCodeGenerator.class.getSimpleName();
        //ImageCodeGenerator
        ValidateCodeGenerator validateCodeGenerator = validateCodeGenerators.get(generatorName);
        if (validateCodeGenerator == null) {
            throw new ValidateCodeException("验证码生成器" + generatorName + "不存在");
        }
        return (C) validateCodeGenerator.generate(request);
    }

    /**
     * 保存校验码
     *
     * @param request
     * @param validateCode
     */
    private void save(ServletWebRequest request, C validateCode) {
        ValidateCode code = new ValidateCode(validateCode.getCode(), validateCode.getExpireTime());
        //校验码存入session
        //sessionStrategy.setAttribute(request, getSessionKey(request), code);
        validateCodeRepository.save(request, code, getValidateCodeType(request));

    }

    /**
     * 构建验证码放入session时的key
     *
     * @param request
     * @return
     */
    private String getSessionKey(ServletWebRequest request) {
        return SESSION_KEY_PREFIX + getValidateCodeType(request).toString().toUpperCase();
    }

    /**
     * 发送校验码，由子类实现
     *
     * @param request
     * @param validateCode
     * @throws Exception
     */
    protected abstract void send(ServletWebRequest request, C validateCode) throws Exception;

    /**
     * 根据请求的url获取校验码的类型
     *
     * @param request
     * @return
     */
    private ValidateCodeType getValidateCodeType(ServletWebRequest request) {
        // ImageCodeProcess  --> image
        String type = StringUtils.substringBefore(getClass().getSimpleName(), "CodeProcessor");

        //IMAGE
        return ValidateCodeType.valueOf(type.toUpperCase());
    }

    /**
     * 校验验证码
     *
     * @param servletWebRequest
     * @throws Exception
     */
    @Override
    public void validate(ServletWebRequest request) {
        ValidateCodeType processorType = getValidateCodeType(request);
        //String sessionKey = getSessionKey(request);
        //从session中获取
        //C codeInSession = (C) sessionStrategy.getAttribute(request, sessionKey);
        C codeInSession = (C) validateCodeRepository.get(request, processorType);
        String codeInRequest;
        try {
            codeInRequest = ServletRequestUtils.getStringParameter(request.getRequest(),
                    processorType.getParamNameOnValidate());
        } catch (ServletRequestBindingException e) {
            throw new ValidateCodeException("获取验证码的值失败");
        }

        if (StringUtils.isBlank(codeInRequest)) {
            throw new ValidateCodeException("验证码的值不能为空");
        }

        if (codeInSession == null) {
            throw new ValidateCodeException("验证码不存在");
        }

        if (codeInSession.isExpried()) {
            //sessionStrategy.removeAttribute(request, sessionKey);
            validateCodeRepository.remove(request, processorType);
            throw new ValidateCodeException("验证码已过期");
        }

        if (!StringUtils.equalsIgnoreCase(codeInSession.getCode(), codeInRequest)) {
            throw new ValidateCodeException("验证码不匹配");
        }
        validateCodeRepository.remove(request, processorType);
    }

}
