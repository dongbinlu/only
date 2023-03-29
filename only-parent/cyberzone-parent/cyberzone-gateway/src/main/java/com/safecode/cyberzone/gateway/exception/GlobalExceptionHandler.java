package com.safecode.cyberzone.gateway.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.HandlerMethod;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.safecode.cyberzone.base.dto.ResponseData;

/**
 * <p>
 * Title: GlobalExceptionHandler
 * </p>
 * <p>
 * Description: 全局捕获异常 使用AOP技术，采用异常通知
 * </p>
 *
 * @author ludongbin
 * @date 2018年12月26日
 */
@ControllerAdvice(basePackages = "com.safecode.cyberzone.gateway")
public class GlobalExceptionHandler {

    private Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // 表示拦截异常
    @ExceptionHandler(CustomException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseData<CustomException> handlerException(CustomException e, HandlerMethod handlerMethod) {
        ResponseData<CustomException> data = new ResponseData<CustomException>();
        log.error("***全局异常处理***");
        log.error("errorCode：" + HttpStatus.INTERNAL_SERVER_ERROR.value());
        log.error("errorClass：" + handlerMethod.getBean().getClass());
        log.error("errorMethod：" + handlerMethod.getMethod().getName());
        log.error("errorMsg：", e);

        data.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        System.err.println(e.getBody());
        ResponseData responseData = (ResponseData) JSON.parseObject(e.getBody(), ResponseData.class);
//		{"code":500,"msg":"邮箱已被占用","data":null}
        data.setMsg(responseData.getMsg());
        return data;
    }

}
