package com.safecode.security.user.exception;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.HandlerMethod;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, Object> handlerException(Exception e, HandlerMethod handlerMethod) {
        log.error("全局异常处理 : ", e);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("time", new Date().getTime());
        if (e instanceof BindException) {
            BindException ex = (BindException) e;
            map.put("message", "校验错误:" + ex.getBindingResult().getFieldError().getDefaultMessage());
        } else {
            map.put("message", e.getMessage());
        }
        return map;
    }
}
