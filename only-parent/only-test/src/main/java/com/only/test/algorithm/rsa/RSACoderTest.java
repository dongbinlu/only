package com.only.test.algorithm.rsa;


import org.junit.Before;
import org.junit.Test;

import java.util.Map;

public class RSACoderTest {


    private String publicKey;

    private String privateKey;

    @Before
    public void setUp() throws Exception {

        Map<String, Object> keyMap = RSACoder.initkey();
        publicKey = RSACoder.getPublicKey(keyMap);
        privateKey = RSACoder.getPrivateKey(keyMap);

        System.err.println("公钥：\n\r" + publicKey);
        System.err.println("私钥：\n\r" + privateKey);

    }

    @Test
    public void testEncrypt() throws Exception {

        String inputStr = "abc";
        byte[] data = inputStr.getBytes();

        byte[] encodeData = RSACoder.encryptByPublicKey(data, publicKey);
        String encodeStr = new String(encodeData, "UTF-8");

        byte[] decodeData = RSACoder.decryByPrivateKey(encodeData, privateKey);

        String outputStr = new String(decodeData);
        System.err.println("加密前：" + inputStr + "\n\r" + "加密后：" + encodeStr + "\n\r" + "解密后：" + outputStr);

    }

    @Test
    public void testSign() throws Exception {

        String inputStr = "abc";
        byte[] data = inputStr.getBytes();
        System.err.println("签名前：\n\r" + inputStr);
        // 产生签名
        String sign = RSACoder.sign(data, privateKey);
        System.err.println("签名后：\n\r" + sign);

        // 验证签名
        boolean status = RSACoder.verfify(data, publicKey, sign);
        System.err.println("状态：\n\r" + status);

    }


}
