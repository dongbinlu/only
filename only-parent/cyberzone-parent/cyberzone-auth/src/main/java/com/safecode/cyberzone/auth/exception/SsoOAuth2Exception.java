package com.safecode.cyberzone.auth.exception;

import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.safecode.cyberzone.auth.handler.SsoOAuthExceptionJacksonSerializer;

/**
 * 自定义Oauth2异常
 *
 * @author v_boy
 */
@JsonSerialize(using = SsoOAuthExceptionJacksonSerializer.class)
public class SsoOAuth2Exception extends OAuth2Exception {
    private static final long serialVersionUID = 3301560361389123241L;

    public SsoOAuth2Exception(String msg, Throwable t) {
        super(msg, t);
    }

    public SsoOAuth2Exception(String msg) {
        super(msg);
    }
}
