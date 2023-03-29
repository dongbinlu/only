package com.safecode.security.user.exception;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.HandlerMethod;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, Object> handlerException(Exception e, HandlerMethod handlerMethod) {
        log.error("SYSTEM ERROR : ", e);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("message", "SYSTEM ERROR");
        map.put("time", new Date().getTime());
        return map;
    }
}
