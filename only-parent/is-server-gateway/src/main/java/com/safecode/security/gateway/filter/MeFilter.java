package com.safecode.security.gateway.filter;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

@Component
public class MeFilter extends ZuulFilter {

    @Override
    public Object run() throws ZuulException {
        RequestContext requestContext = RequestContext.getCurrentContext();
        String username = requestContext.getZuulRequestHeaders().get("username");
        if (StringUtils.isNotBlank(username)) {
            requestContext.setResponseBody("{\"username\":\"" + username + "\"}");
            requestContext.setSendZuulResponse(false);
            requestContext.setResponseStatusCode(HttpStatus.OK.value());
            requestContext.getResponse().setContentType("applicationi/json;charset-UTF-8");
        } else {
            requestContext.setSendZuulResponse(false);
            requestContext.setResponseStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            requestContext.getResponse().setContentType("applicationi/json;charset-UTF-8");
        }
        return null;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        return StringUtils.equals(request.getRequestURI(), "/user/me");
    }

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 4;
    }

}
