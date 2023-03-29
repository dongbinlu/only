package com.safecode.cyberzone.auth.validate.code.image;

import java.awt.image.BufferedImage;
import java.time.LocalDateTime;

import com.safecode.cyberzone.auth.validate.code.ValidateCode;

/**
 * 1， 开发生成图形验证码接口 生成图形验证码： a.根据随机数生成图片 b.将随机数存到session中 c.将生成的图片写到接口的响应中
 *
 * @author v_boy
 */
public class ImageCode extends ValidateCode {

    /**
     *
     */
    private static final long serialVersionUID = -4086230765303188904L;
    // 图片
    private BufferedImage image;

    // 此构造是校验时间是否过期
    public ImageCode(BufferedImage image, String code, int expireIn) {
        super(code, expireIn);
        this.image = image;
    }

    // 存过期时间时 是一个固定的时间
    public ImageCode(BufferedImage image, String code, LocalDateTime expireTime) {
        super(code, expireTime);
        this.image = image;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }
}
