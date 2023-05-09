package com.only.test.algorithm.aes;

import com.sun.org.apache.xml.internal.security.utils.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class AesDemo {
    // DES加密算法,key的大小必须是8个字节

    public static void main(String[] args) throws Exception {
        String input = "硅谷";
        // AES加密算法，比较高级，所以key的大小必须是16个字节
        String key = "1234567812345678";

        String transformation = "AES"; // 9PQXVUIhaaQ=
        // 指定获取密钥的算法
        String algorithm = "AES";
        // 先测试加密，然后在测试解密
        String encryptDES = encryptDES(input, key, transformation, algorithm);
        System.out.println("加密:" + encryptDES);
        String s = dncryptDES(encryptDES, key, transformation, algorithm);
        System.out.println("解密:" + s);

    }

    /**
     * 随机生成秘钥
     */

    public static void getkey() {

        try {

            KeyGenerator kg = KeyGenerator.getInstance("aes");

            kg.init(128);

            //要生成多少位，只需要修改这里即可128, 192或256

            SecretKey sk = kg.generateKey();

            byte[] b = sk.getEncoded();

            String s = byteToHexstring(b);

            System.out.println(s);

            System.out.println("十六进制密钥长度为" + s.length());

            System.out.println("二进制密钥的长度为" + s.length() * 4);

        } catch (Exception e) {

            e.printStackTrace();

            System.out.println("没有此算法。");

        }

    }

    public static String byteToHexstring(byte[] bytes) {

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            String strhex = Integer.toHexString(bytes[i]);
            if (strhex.length() > 3) {
                sb.append(strhex.substring(6));
            } else {
                if (strhex.length() < 2) {
                    sb.append("0" + strhex);
                } else {
                    sb.append(strhex);
                }
            }
        }
        return sb.toString();
    }


    /**
     * 使用DES加密数据
     *
     * @param input          : 原文
     * @param key            : 密钥(DES,密钥的长度必须是8个字节)
     * @param transformation : 获取Cipher对象的算法
     * @param algorithm      : 获取密钥的算法
     * @return : 密文
     * @throws Exception
     */
    private static String encryptDES(String input, String key, String transformation, String algorithm) throws Exception {
        // 获取加密对象
        Cipher cipher = Cipher.getInstance(transformation);
        // 创建加密规则
        // 第一个参数key的字节
        // 第二个参数表示加密算法
        SecretKeySpec sks = new SecretKeySpec(key.getBytes(), algorithm);
        // ENCRYPT_MODE：加密模式
        // DECRYPT_MODE: 解密模式
        // 初始化加密模式和算法
        cipher.init(Cipher.ENCRYPT_MODE, sks);
        // 加密
        byte[] bytes = cipher.doFinal(input.getBytes());

        // 输出加密后的数据
        String encode = Base64.encode(bytes);

        return encode;
    }

    /**
     * 使用DES解密
     *
     * @param input          : 密文
     * @param key            : 密钥
     * @param transformation : 获取Cipher对象的算法
     * @param algorithm      : 获取密钥的算法
     * @throws Exception
     * @return: 原文
     */
    private static String dncryptDES(String input, String key, String transformation, String algorithm) throws Exception {
        // 1,获取Cipher对象
        Cipher cipher = Cipher.getInstance(transformation);
        // 指定密钥规则
        SecretKeySpec sks = new SecretKeySpec(key.getBytes(), algorithm);
        cipher.init(Cipher.DECRYPT_MODE, sks);
        // 3. 解密
        byte[] bytes = cipher.doFinal(Base64.decode(input));

        return new String(bytes);
    }
}
