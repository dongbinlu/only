package com.safecode.es.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.safecode.es.service.ReIndexService;

@RestController
@RequestMapping("/index")
public class IndexController {

    @Autowired
    private ReIndexService reIndexService;

    @GetMapping
    public String index() {
        reIndexService.reIndexBooks();
        return "success";
    }

}
