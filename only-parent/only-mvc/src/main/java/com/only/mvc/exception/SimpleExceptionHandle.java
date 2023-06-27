package com.only.mvc.exception;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SimpleExceptionHandle implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {


        ModelAndView mv = new ModelAndView("/WEB-INF/jsp/error.jsp");
        if (ex instanceof IllegalArgumentException) {
            mv.addObject("errorType", "参数非法");
        } else {
            mv.addObject("errorType", ex.getClass().getSimpleName());
        }
        mv.addObject("msg", ex.getMessage());

        return mv;
    }
}
