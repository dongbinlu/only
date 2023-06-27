package com.only.mvc.servletInitializer;

import javax.servlet.*;
import java.io.IOException;

public class AngleFilter implements Filter {


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("angle filter doFilter");
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("angle filter init");
    }

    @Override
    public void destroy() {
        System.out.println("angle filter destroy");
    }
}
