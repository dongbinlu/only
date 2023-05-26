package com.only.order.center.exception;


import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    @ExceptionHandler(Exception.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Map<String, Object> globalExceptionHandler(Exception e) {

        log.error("GlobalExceptionHandler", e);

        HashMap<String, Object> map = Maps.newHashMap();
        map.put("code", HttpStatus.INTERNAL_SERVER_ERROR);
        map.put("msg", e.getMessage());

        return map;
    }
}
