package com.safecode.security.permission.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.HandlerMethod;

import com.safecode.security.permission.common.JsonData;

@ControllerAdvice
public class GlobalExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public JsonData handlerException(RuntimeException e, HandlerMethod handlerMethod) {

        String defaultMsg = "系统异常 !!!";
        JsonData result = null;
        if (e instanceof PermissionException || e instanceof ParamException || e instanceof NullPointerException) {
            result = JsonData.fail(e.getMessage());
        } else {
            result = JsonData.fail(defaultMsg);
        }
        logger.error("*********全局异常处理*********");
        logger.error("errorClass：" + handlerMethod.getBean().getClass());
        logger.error("errorMethod：" + handlerMethod.getMethod().getName());
        logger.error("errorMsg：", e);
        return result;

    }

}
