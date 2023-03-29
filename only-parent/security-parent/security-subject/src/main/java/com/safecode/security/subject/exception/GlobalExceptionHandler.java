package com.safecode.security.subject.exception;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.HandlerMethod;

@ControllerAdvice
public class GlobalExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, Object> handlerException(RuntimeException e, HandlerMethod handlerMethod) {

        Map<String, Object> map = new HashMap<>();
        map.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
        map.put("message", e.getMessage());

        logger.error("***全局异常处理***");
        logger.error("errorCode：" + HttpStatus.INTERNAL_SERVER_ERROR.value());
        logger.error("errorClass：" + handlerMethod.getBean().getClass());
        logger.error("errorMethod：" + handlerMethod.getMethod().getName());
        logger.error("errorMsg：", e);
        return map;

    }

}
