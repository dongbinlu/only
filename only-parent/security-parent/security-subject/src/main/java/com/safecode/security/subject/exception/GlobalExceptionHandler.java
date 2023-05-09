package com.safecode.security.subject.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.HandlerMethod;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, Object> handlerException(Exception e, HandlerMethod handlerMethod) {

        logger.error("***全局异常处理***");
        logger.error("errorCode：" + HttpStatus.INTERNAL_SERVER_ERROR.value());
        logger.error("errorClass：" + handlerMethod.getBean().getClass());
        logger.error("errorMethod：" + handlerMethod.getMethod().getName());
        logger.error("errorMsg：", e);

        Map<String, Object> map = new HashMap<>();
        map.put("code", HttpStatus.INTERNAL_SERVER_ERROR.value());
        if (e instanceof UserNotExistException) {
            map.put("message", e.getMessage());
        } else if (e instanceof MethodArgumentNotValidException) {// 参数校验，需要在方法参数上加@Valid 注解
            MethodArgumentNotValidException ex = (MethodArgumentNotValidException) e;
            map.put("message", "校验错误:" + ex.getBindingResult().getFieldError().getDefaultMessage());
        }

        return map;

    }

}
