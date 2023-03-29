package com.safecode.security.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
    /**
     * <p>@Title: index<p>
     * <p>@Description: 去首页</p>
     *
     * @return
     * @date 2019年11月4日 下午1:25:58
     */
    @GetMapping("")
    public String index() {
        return "index";
    }

}
