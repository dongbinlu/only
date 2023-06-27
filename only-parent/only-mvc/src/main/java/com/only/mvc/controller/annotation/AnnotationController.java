package com.only.mvc.controller.annotation;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AnnotationController {

    @RequestMapping(value = "/index",method = RequestMethod.GET)
    public ModelAndView index(){
        ModelAndView mv = new ModelAndView("/WEB-INF/jsp/index.jsp");
        mv.addObject("name", "jojo");
        return mv;
    }

}
