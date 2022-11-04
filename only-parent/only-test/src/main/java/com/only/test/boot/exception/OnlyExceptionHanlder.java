package com.only.test.boot.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class OnlyExceptionHanlder {
    /**
     * 浏览器和其他客户端都返回了json 数组，不满足自适应
     *
     * @param e
     * @param request
     * @return
     */
    @ExceptionHandler(value = ParamException.class)
    public String paramException(ParamException e, HttpServletRequest request) {
        Map<String, Object> retInfo = new HashMap<>();
        retInfo.put("code",500);
        retInfo.put("msg", e.getMessage());
        request.setAttribute("javax.servlet.error.status_code", 500);
        request.setAttribute("ext", retInfo);
        return "forward:/error";
    }
}