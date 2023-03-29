package com.safecode.cyberzone.auth.validate.code.image;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

import com.safecode.cyberzone.auth.validate.code.impl.AbstractValidateCodeProcessor;

/**
 * 图片验证码处理器
 *
 * @author v_boy
 */
@Component("imageValidateCodeProcessor")
public class ImageCodeProcessor extends AbstractValidateCodeProcessor<ImageCode> {
    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 发送图形验证码，将其写到响应中
     */
    @Override
    protected void send(ServletWebRequest request, ImageCode imageCode) throws Exception {
        HttpServletResponse response = request.getResponse();
        response.setContentType("image/jpeg");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-cache");
        logger.info("图片验证码：" + imageCode.getCode());
        ImageIO.write(imageCode.getImage(), "JPEG", response.getOutputStream());

    }

}
