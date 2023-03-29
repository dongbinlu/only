package com.safecode.security.gateway.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.safecode.security.gateway.entity.TokenInfo;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AuthorizationFilter extends ZuulFilter {
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public Object run() throws ZuulException {

        log.info("authorization start");

        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        if (isNeedAuth(request)) {
            // 如果需要
            TokenInfo tokenInfo = (TokenInfo) request.getAttribute("tokenInfo");

            if (tokenInfo != null && tokenInfo.isActive()) {

                // 如果用户信息正常，判断是否有权限
                if (!hasPermission(tokenInfo, request)) {
                    log.info("audit log update fail 403");
                    handlerError(HttpStatus.FORBIDDEN.value(), requestContext, "forbidden");
                }
                requestContext.addZuulRequestHeader("username", tokenInfo.getUser_name());
            } else {
                if (!StringUtils.startsWith(request.getRequestURI(), "/auth")) {
                    log.info("audit log update fail 401");
                    handlerError(HttpStatus.UNAUTHORIZED.value(), requestContext, "unauthorized");
                }
            }
        }

        return null;
    }

    /*
     * 判断是否有权限，实际业务开发
     */
    private boolean hasPermission(TokenInfo tokenInfo, HttpServletRequest request) {
        // 随机数和2取模
        // return RandomUtils.nextInt() % 2 == 0;
        return true;
    }

    /*
     * 处理401，403
     */
    private void handlerError(int status, RequestContext requestContext, String msg) {

        HttpServletResponse response = requestContext.getResponse();
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(status);
        try {
            response.getWriter().write(objectMapper.writeValueAsString(msg));
        } catch (Exception e) {
            log.info("handLerError", e);
        }
        // 终止过滤器
        requestContext.setSendZuulResponse(false);

    }

    /*
     * 判断是否需要认证，譬如商品预览...不需要，实际业务书写
     */
    private boolean isNeedAuth(HttpServletRequest request) {

        return true;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public int filterOrder() {
        return 3;
    }

    @Override
    public String filterType() {
        return "pre";
    }

}
