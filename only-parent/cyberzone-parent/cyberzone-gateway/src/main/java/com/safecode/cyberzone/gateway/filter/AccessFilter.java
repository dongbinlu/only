package com.safecode.cyberzone.gateway.filter;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

public class AccessFilter extends ZuulFilter {
    private static Logger logger = LoggerFactory.getLogger(AccessFilter.class);

    @Override
    public Object run() {
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();

        String remoteAddr = request.getRemoteAddr();
        requestContext.getZuulRequestHeaders().put("HTTP_X_FORWARDED_FOR", remoteAddr);

        logger.info("send {} request to {}", request.getMethod(), request.getRequestURL().toString());
//		String auth = request.getHeader("Authorization");
//		boolean haveOauth2Token = auth != null;
//		boolean haveAccessToken = request.getParameter("access_token") != null;
//
//		if (!haveOauth2Token && !haveAccessToken) {
//			logger.warn("Authorization token is empty");
//			requestContext.setSendZuulResponse(false);
//			requestContext.setResponseStatusCode(401);
//			requestContext.setResponseBody("Authorization token is empty");
//			return null;
//		}
//		logger.info("Authorization token is ok");
        return null;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public String filterType() {
        return "pre";
    }

}
