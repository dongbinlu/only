package com.only.test.algorithm.rsa;

import com.google.common.collect.Maps;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

/**
 * RSA安全编码组件
 */
public class RSACoder {

    private static final String KEY_ALGORITHM = "RSA";

    private static final String SIGNATURE_ALGORITHM = "MD5withRSA";

    private static final String PUBLIC_KEY = "RSAPublicKey";

    private static final String PRIVATE_KEY = "RSAPrivateKey";

    public static Map<String, Object> initkey() throws Exception {
        // 获取RSA算法的密钥对生成器
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        //设定密钥长度为1024位
        keyPairGen.initialize(1024);

        // 获取RSA密钥对对象
        KeyPair keyPair = keyPairGen.generateKeyPair();

        // 公钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        // 私钥
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        HashMap<String, Object> keyMap = Maps.newHashMap();
        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);

        return keyMap;
    }

    /**
     * 用私钥对信息生成数字签名
     *
     * @param data 加密数据
     * @param key  私钥
     * @return
     */
    public static String sign(byte[] data, String key) throws Exception {
        // 解密由base64编码的私钥
        byte[] keyBytes = decryptBASE64(key);

        // 构造PKCS8EncoderKeySpec对象
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        // KEY_ALGORITHM指定加密算法
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);

        // 获取私钥对象
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);

        // 用私钥对信息生成数字签名
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(privateKey);
        signature.update(data);

        return encryptBASE64(signature.sign());
    }

    /**
     * 校验数字签名
     *
     * @param data 加密数据
     * @param key  公钥
     * @param sign 数字签名
     * @return 校验成功返回true，失败返回false
     */
    public static boolean verfify(byte[] data, String key, String sign) throws Exception {

        //解码由base64编码的公钥
        byte[] keyBytes = decryptBASE64(key);
        //构造x509对象
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        //指定加密算法
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        //获取公钥对象
        PublicKey publicKey = keyFactory.generatePublic(keySpec);

        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(publicKey);
        signature.update(data);

        // 验证签名是否正常
        // 注意这里，并不是通过解签名来解出原文和原文比较，而是进行验证
        return signature.verify(decryptBASE64(sign));


    }

    /**
     * 用公钥加密
     *
     * @param data
     * @param key
     * @return
     */
    public static byte[] encryptByPublicKey(byte[] data, String key) throws Exception {

        // 对公钥解码
        byte[] keyBytes = decryptBASE64(key);
        // 获取公钥
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        Key publicKey = keyFactory.generatePublic(x509KeySpec);

        // 对数据加密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(data);
    }

    /**
     * 用私钥解密
     *
     * @param data
     * @param key
     * @return
     */
    public static byte[] decryByPrivateKey(byte[] data, String key) throws Exception {

        // 对密钥解码
        byte[] keyBytes = decryptBASE64(key);

        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);

        Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);

        //对数据解密
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        return cipher.doFinal(data);
    }

    /**
     * 获取公钥
     *
     * @param keyMap
     * @return
     */
    public static String getPublicKey(Map<String, Object> keyMap) {
        Key key = (Key) keyMap.get(PUBLIC_KEY);
        return encryptBASE64(key.getEncoded());
    }

    /**
     * 获取私钥
     *
     * @param keyMap
     * @return
     */
    public static String getPrivateKey(Map<String, Object> keyMap) {
        Key key = (Key) keyMap.get(PRIVATE_KEY);
        return encryptBASE64(key.getEncoded());
    }

    private static byte[] decryptBASE64(String key) {
        return Base64.decodeBase64(key);
    }

    private static String encryptBASE64(byte[] key) {
        return Base64.encodeBase64String(key);
    }
}
