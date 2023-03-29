package com.safecode.security.gateway.filter;

import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.exception.ZuulException;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AuditLogFilter extends ZuulFilter {

    @Override
    public Object run() throws ZuulException {

        log.info("audit log insert");

        return null;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public int filterOrder() {
        return 2;
    }

    @Override
    public String filterType() {
        return "pre";
    }

}
