package com.safecode.cyberzone.auth.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "cyberzone.security")
public class SecurityProperties {

    // OAuth2认证服务器配置
    private OAuth2Properties oauth2 = new OAuth2Properties();

    // 允许访问的配置
    private PermitProperties permit = new PermitProperties();

    // 其他配置
    private OtherProperties other = new OtherProperties();

    private ValidateCodeProperties code = new ValidateCodeProperties();

    public OAuth2Properties getOauth2() {
        return oauth2;
    }

    public void setOauth2(OAuth2Properties oauth2) {
        this.oauth2 = oauth2;
    }

    public PermitProperties getPermit() {
        return permit;
    }

    public void setPermit(PermitProperties permit) {
        this.permit = permit;
    }

    public OtherProperties getOther() {
        return other;
    }

    public void setOther(OtherProperties other) {
        this.other = other;
    }

    public ValidateCodeProperties getCode() {
        return code;
    }

    public void setCode(ValidateCodeProperties code) {
        this.code = code;
    }

}
