package com.safecode.security.admin.filter;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.safecode.security.admin.entity.TokenInfo;

@Component
public class SessionTokenFilter extends ZuulFilter {

    @Override
    public Object run() throws ZuulException {

        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();

        TokenInfo tokenInfo = (TokenInfo) request.getSession().getAttribute("token");
        if (tokenInfo != null) {
            requestContext.addZuulRequestHeader("Authorization", "Bearer " + tokenInfo.getAccess_token());
        }
        return null;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

}
