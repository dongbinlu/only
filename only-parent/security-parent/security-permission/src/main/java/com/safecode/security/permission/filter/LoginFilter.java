package com.safecode.security.permission.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;

import com.safecode.security.permission.common.JsonData;
import com.safecode.security.permission.common.RequestHolder;
import com.safecode.security.permission.entity.SysUser;
import com.safecode.security.permission.utils.JsonMapper;

public class LoginFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        SysUser user = (SysUser) req.getSession().getAttribute("user");

        if (user == null) {
            res.setStatus(HttpStatus.UNAUTHORIZED.value());
            res.setContentType("application/json;charset=utf-8");
            res.getWriter().write(JsonMapper.obj2String(JsonData.fail("请登录")));
            return;
        }
        RequestHolder.add(user);
        RequestHolder.add(req);
        chain.doFilter(request, response);

    }

    @Override
    public void destroy() {

    }

}
