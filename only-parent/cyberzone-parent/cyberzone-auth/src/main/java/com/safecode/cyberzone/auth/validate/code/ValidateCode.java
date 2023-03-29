package com.safecode.cyberzone.auth.validate.code;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ValidateCode implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 2545764452135537956L;
    // 随机数 存到session中
    private String code;
    // 过期时间，验证码需要一定的时间 在多少秒内过期
    private LocalDateTime expireTime;

    // 此构造是校验时间是否过期
    public ValidateCode(String code, int expireIn) {
        this.code = code;
        this.expireTime = LocalDateTime.now().plusSeconds(expireIn);
    }

    // 存过期时间时 是一个固定的时间
    public ValidateCode(String code, LocalDateTime expireTime) {
        this.code = code;
        this.expireTime = expireTime;
    }

    // 判断验证码是否过期
    public boolean isExpried() {
        return LocalDateTime.now().isAfter(expireTime);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDateTime getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(LocalDateTime expireTime) {
        this.expireTime = expireTime;
    }

}
