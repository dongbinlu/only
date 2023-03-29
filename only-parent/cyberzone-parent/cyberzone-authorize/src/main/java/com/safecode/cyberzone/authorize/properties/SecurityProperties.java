package com.safecode.cyberzone.authorize.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "cyberzone.security")
public class SecurityProperties {

    // 其他配置
    private OtherProperties other = new OtherProperties();

    // 允许访问的配置
    private PermitProperties permit = new PermitProperties();

    public OtherProperties getOther() {
        return other;
    }

    public void setOther(OtherProperties other) {
        this.other = other;
    }

    public PermitProperties getPermit() {
        return permit;
    }

    public void setPermit(PermitProperties permit) {
        this.permit = permit;
    }

}
