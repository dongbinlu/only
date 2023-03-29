package com.safecode.security.subject.filter;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CorsFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletResponse res = (HttpServletResponse) response;

        // 带cookie的时候，origin必须使用全匹配，不能使用*号
        // 表示允许那个域跨域调用
        // 用*号表示所有域,当出现cookie跨域时，*号是不能满足需求的，必须是指定的域

        // 当发生跨域请求时，浏览器的请求头会出现Origin字段
        HttpServletRequest req = (HttpServletRequest) request;
        String origin = req.getHeader("Origin");

        if (!StringUtils.isEmpty(origin)) {
            res.addHeader("Access-Control-Allow-Origin", origin);
        }

        // 指定允许的方法 ， 用*号表示所有方法
        res.addHeader("Access-Control-Allow-Methods", "*");

        // 告诉浏览器，允许这个header
        // 获取header
        String headers = req.getHeader("Access-Control-Request-Headers");
        if (!StringUtils.isEmpty(headers)) {
            res.addHeader("Access-Control-Allow-Headers", headers);
        }

        // 预检命令会影响效率，http里面增加了一个响应头，可以用户缓存预检命令
        res.addHeader("Access-Control-Max-Age", "3600");

        // enable cookie 带cookie的时候，必须携带Credentials=true这个字段
        res.addHeader("Access-Control-Allow-Credentials", "true");

        chain.doFilter(request, response);

    }

    @Override
    public void destroy() {

    }

}
