package com.only.mvc.plus.controller;

import com.only.mvc.plus.anno.MvcMapping;
import com.only.mvc.plus.entity.BlogDoc;
import com.only.mvc.plus.mvc.FreemarkerView;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class BlogControl {
    List<BlogDoc> docs = new ArrayList<>();

    @MvcMapping("/edit")
    public FreemarkerView openEditPage(String user) {
        FreemarkerView freemarkerView = new FreemarkerView("edit.ftl");
        freemarkerView.setModel("authorName", user);
        freemarkerView.setModel("user", user);
        return freemarkerView;
    }

    @MvcMapping("/list")
    public FreemarkerView openDocList(String author) {
        List<BlogDoc> result = new ArrayList<>();
        if (author != null) {
            for (BlogDoc doc : docs) {
                if (author.equals(doc.getAuthor())) {
                    result.add(doc);
                }
            }
        } else {
            result.addAll(docs);
        }
        FreemarkerView freemarkerView = new FreemarkerView("docList.ftl");
        freemarkerView.setModel("authorName", author);
        freemarkerView.setModel("docs", result);
        return freemarkerView;
    }

    @MvcMapping("/save")
    public void openEditPage(String title, String author, String content, HttpServletResponse res) {
        BlogDoc doc = new BlogDoc(title, author, content, new Date());
        docs.add(doc);
        try {
            res.sendRedirect("/list");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
