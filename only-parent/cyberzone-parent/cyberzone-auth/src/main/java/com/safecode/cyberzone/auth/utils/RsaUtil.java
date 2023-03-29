package com.safecode.cyberzone.auth.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.Security;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;

public class RsaUtil {
    public static final String PADDING = "RSA/ECB/PKCS1Padding";
    public static final String PROVIDER = "BC";
    public static final Provider provider = new org.bouncycastle.jce.provider.BouncyCastleProvider();

    static {
        Security.addProvider(provider);
    }

    /**
     * 解密数据
     *
     * @param encrypted
     * @param privateKey
     * @return
     * @throws Exception
     */
    public static String decrypt(String encrypted, RSAPrivateKey privateKey) throws Exception {
        Cipher cipher;
        try {
            try {
                cipher = Cipher.getInstance(PADDING);
            } catch (Exception e) {
                cipher = Cipher.getInstance(PADDING, PROVIDER);
            }
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException("RSA algorithm not supported", e);
        }
        String[] blocks = encrypted.split("\\s");
        StringBuffer result = new StringBuffer();

        try {
            for (int i = 0; i < blocks.length; i++) {
                byte[] data = Base64.decodeBase64(blocks[i].getBytes());
                byte[] finalBytes = cipher.doFinal(data);
                result.append(new String(finalBytes, "UTF-8"));
            }
        } catch (GeneralSecurityException e) {
            throw new RuntimeException("Decrypt error", e);
        }
        return result.toString();
    }

    /**
     * 通过私钥PEM文件流还原私钥
     *
     * @param inputStream
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @throws IOException
     */
    public static RSAPrivateKey getPrivateKey(InputStream inputStream)
            throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        StringBuffer publicKey = new StringBuffer();
        String s;
        while ((s = br.readLine()) != null) {
            if (s.charAt(0) != '-') {
                publicKey.append(s + "\r");
            }
        }
        byte[] keyBytes = Base64.decodeBase64(publicKey.toString().getBytes());

        return getPrivateKey(keyBytes);
    }

    /**
     * 通过私钥byte[]还原私钥
     *
     * @param keyBytes
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static RSAPrivateKey getPrivateKey(byte[] keyBytes)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        return (RSAPrivateKey) privateKey;
    }
}
