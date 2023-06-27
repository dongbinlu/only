package com.only.mvc.plus.controller;

import com.only.mvc.plus.anno.MvcMapping;
import com.only.mvc.plus.entity.BlogDoc;
import com.only.mvc.plus.mvc.FreemarkerView;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Controller
public class MvcControl {

    @MvcMapping("/luban.do")
    public FreemarkerView open(String name) {
        FreemarkerView freemarkerView = new FreemarkerView("luban.ftl");
        freemarkerView.setModel("name", name);
        return freemarkerView;
    }


    @MvcMapping("/luban-plus.do")
    public FreemarkerView open(String name, BlogDoc doc, HttpServletRequest request, HttpServletResponse resp) {
        FreemarkerView freemarkerView = new FreemarkerView("luban.ftl");
        freemarkerView.setModel("name", name);
        return freemarkerView;
    }
}
