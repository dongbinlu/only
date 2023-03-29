package com.safecode.security.core.validate.code;

import org.springframework.web.context.request.ServletWebRequest;

/**
 * 校验码处理器 ， 封装不同校验码的处理逻辑
 *
 * @author v_boy
 * 接口：用来明确验证码处理器该具有的功能，相当于在定义验证码处理器的标准功能
 * 发送验证码不是验证码处理器该干的事，因为验证码处理器并不知道自己该发什么样的验证码出去，
 * 所以验证码处理器不确定此功能
 */

public interface ValidateCodeProcessor {

    /**
     * 创建校验码
     *
     * @param request
     * @throws Exception
     */
    void create(ServletWebRequest request) throws Exception;

    /**
     * 校验验证码
     *
     * @param servletWebRequest
     * @throws Exceptionr
     */
    void validate(ServletWebRequest equest);

}
