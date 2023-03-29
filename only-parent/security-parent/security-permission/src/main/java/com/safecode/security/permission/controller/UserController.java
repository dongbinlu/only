package com.safecode.security.permission.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.safecode.security.permission.common.JsonData;
import com.safecode.security.permission.common.RequestHolder;
import com.safecode.security.permission.entity.SysUser;
import com.safecode.security.permission.service.SysUserService;
import com.safecode.security.permission.utils.MD5Util;

@RestController
public class UserController {

    @Autowired
    private SysUserService sysUserService;

    @PostMapping("/login")
    public JsonData login(HttpServletRequest request) {

        String username = request.getParameter("username").trim();
        String password = request.getParameter("password").trim();

        SysUser sysUser = sysUserService.findBykeyword(username);

        String errorMsg = "";

        if (StringUtils.isBlank(username)) {
            errorMsg = "用户名不可以为空";
        } else if (StringUtils.isBlank(password)) {
            errorMsg = "密码不可以为空";
        } else if (sysUser == null) {
            errorMsg = "查询不到指定用户";
        } else if (!sysUser.getPassword().equals(MD5Util.encrypt(password))) {
            errorMsg = "用户名或密码错误";
        } else if (sysUser.getStatus() != 1) {
            errorMsg = "用户已冻结，请联系管理员";
        } else {
            // login success
            request.getSession().setAttribute("user", sysUser);
            return JsonData.success();
        }

        return JsonData.fail(errorMsg);
    }

    @GetMapping("/logout")
    public JsonData logout(HttpServletRequest request) {
        request.getSession().invalidate();
        return JsonData.success();
    }

    @GetMapping("/isLogin")
    public JsonData isLogin() {
        return JsonData.success(RequestHolder.getCurrentUser());
    }

}
