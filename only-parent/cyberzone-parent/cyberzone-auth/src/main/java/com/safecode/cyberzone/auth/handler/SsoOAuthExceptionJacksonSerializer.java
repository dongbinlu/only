package com.safecode.cyberzone.auth.handler;

import java.io.IOException;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.web.util.HtmlUtils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.safecode.cyberzone.auth.exception.SsoOAuth2Exception;

/**
 * 自定义SsoOAuth2Exception的序列化类
 *
 * @author v_boy
 */
public class SsoOAuthExceptionJacksonSerializer extends StdSerializer<SsoOAuth2Exception> {
    private static final long serialVersionUID = -7574710624091536518L;

    protected SsoOAuthExceptionJacksonSerializer() {
        super(SsoOAuth2Exception.class);
    }

    @Override
    public void serialize(SsoOAuth2Exception e, JsonGenerator jgen, SerializerProvider serializerProvider)
            throws IOException {
        jgen.writeStartObject();
        jgen.writeObjectField("code", HttpStatus.UNAUTHORIZED.value());
        String errorMessage = e.getOAuth2ErrorCode();
        if (errorMessage != null) {
            errorMessage = HtmlUtils.htmlEscape(errorMessage);
        }
        Throwable cause = e.getCause();
        System.err.println(e.getMessage());
        if (cause instanceof InvalidGrantException && "User is disabled".equals(e.getMessage())) {
            jgen.writeStringField("msg", "账号未开启");
        } else if (cause instanceof InvalidGrantException && "User account has expired".equals(e.getMessage())) {
            jgen.writeStringField("msg", "账号已过期");
        } else if (cause instanceof InvalidGrantException && "User credentials have expired".equals(e.getMessage())) {
            jgen.writeStringField("msg", "密码已过期");
        } else if (cause instanceof InvalidGrantException && "User account is locked".equals(e.getMessage())) {
            jgen.writeStringField("msg", "账号已锁定");
        } else if (cause instanceof SsoOAuth2Exception) {
            jgen.writeStringField("msg", e.getMessage());
        } else if (cause instanceof InvalidGrantException) {
            jgen.writeStringField("msg", "用户名或密码错误");
        } else {
            jgen.writeStringField("msg", "无效的token");
        }
        // jgen.writeStringField("msg", errorMessage);
        jgen.writeStringField("data", null);
        if (e.getAdditionalInformation() != null) {
            for (Map.Entry<String, String> entry : e.getAdditionalInformation().entrySet()) {
                String key = entry.getKey();
                String add = entry.getValue();
                jgen.writeStringField(key, add);
            }
        }
        jgen.writeEndObject();
    }
}
