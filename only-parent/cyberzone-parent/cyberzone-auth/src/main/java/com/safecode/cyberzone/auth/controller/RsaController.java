package com.safecode.cyberzone.auth.controller;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.safecode.cyberzone.base.dto.ResponseData;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/rsa")
@Slf4j
public class RsaController {

    /**
     * 公钥文件
     */
    private final static String RSA_PUBLIC_KEY = "/rsa/rsa_1024_pub.pem";

    /**
     * 获取公钥
     *
     * @return
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @GetMapping("/rsaKeyPub")
    public ResponseData getRsaKeyPublic() {
        return new ResponseData(HttpStatus.OK.value(), "获取公钥成功", getRsaKeyPublic(RSA_PUBLIC_KEY));
    }

    public static String getRsaKeyPublic(String rsaKeyResource) {
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        StringBuffer buffer = new StringBuffer();
        try {
            is = RsaController.class.getResourceAsStream(rsaKeyResource);
            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);
            String str;
            while ((str = br.readLine()) != null) {
                buffer.append(str);
            }
        } catch (Exception e) {
            log.error("读取公钥异常", e);
            throw new RuntimeException("获取公钥异常");
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
                if (isr != null) {
                    isr.close();
                }
                if (is != null) {
                    is.close();
                }
            } catch (Exception e) {
                log.error("读取公钥异常", e);
                throw new RuntimeException("获取公钥异常");
            }
        }

        return buffer.toString();

    }

}
