package com.only.test.boot.controller;

import com.only.spring.boot.autoconfigure.OnlySessionTemplate;
import com.only.test.boot.DevManage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {
    
    @Autowired
    private OnlySessionTemplate onlySessionTemplate;

    @Autowired
    private DevManage devManage;

    @GetMapping("/hello")
    public int hello(Integer timeout) {

        ThreadLocal<Integer> timeoutHolder = devManage.getTimeoutHolder();
        if (timeout != null) {
            timeoutHolder.set(timeout);
        }
        devManage.send();

        timeoutHolder.remove();

        return timeout;
    }

    @GetMapping("/starter")
    public Object starter(){
        return onlySessionTemplate.delete();
    }

}
