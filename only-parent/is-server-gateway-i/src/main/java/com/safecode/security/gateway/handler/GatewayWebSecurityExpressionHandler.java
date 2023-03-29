package com.safecode.security.gateway.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.expression.OAuth2WebSecurityExpressionHandler;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Component;

import com.safecode.security.gateway.service.PermissionService;

/*
 * 处理web安全表达式
 */
@Component
public class GatewayWebSecurityExpressionHandler extends OAuth2WebSecurityExpressionHandler {

    @Autowired
    private PermissionService permissionService;

    @Override
    protected StandardEvaluationContext createEvaluationContextInternal(Authentication authentication,
                                                                        FilterInvocation invocation) {
        StandardEvaluationContext sec = super.createEvaluationContextInternal(authentication, invocation);
        sec.setVariable("permissionService", permissionService);
        return sec;
    }

}
