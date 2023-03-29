package com.safecode.security.core.properties;

public class SmsCodeProperties {

    // 随机数的长度
    private int length = 4;
    // 过期时间
    private int expireIn = 180;
    // 哪些url需要图形验证码
    private String url;

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getExpireIn() {
        return expireIn;
    }

    public void setExpireIn(int expireIn) {
        this.expireIn = expireIn;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
