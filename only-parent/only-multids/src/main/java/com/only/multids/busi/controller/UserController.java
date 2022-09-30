package com.only.multids.busi.controller;

import com.only.multids.annotation.Router;
import com.only.multids.busi.bean.User;
import com.only.multids.busi.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {


    @Autowired
    private UserServiceImpl userService;


    /**
     * 新增
     *
     * @param user
     * @return
     */
    @PostMapping
    @Router(routingFiled = "userId")
    public void insertUser(User user) {
        userService.insert(user);
    }


}
