package com.safecode.security.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    /*
     * 去首页
     */
    @GetMapping("")
    public String index() {
        return "index";
    }

}
