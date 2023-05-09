package com.only.test.algorithm.aes;

import javax.xml.bind.DatatypeConverter;

public class Test {

    public static void main(String[] args) {
        String str = "This is Test String_123.AbcE";
        //BASE64编码与解码
        String encode = DatatypeConverter.printBase64Binary(str.getBytes());
        System.out.println("BASE64编码: " + encode);
        byte[] decode = DatatypeConverter.parseBase64Binary(encode);
        System.out.println("BASE64解码：" + new String(decode));

        //16进制编码与解码
        String encode16 = DatatypeConverter.printHexBinary(str.getBytes());
        System.out.println("16进制编码:" + encode16);
        byte[] decode16 = DatatypeConverter.parseHexBinary(encode16);
        System.out.println("16进制:" + new String(decode16));

        // 539B333B39706D149028CFE1D9D4A407
        // 8000000000000000000000000000000000000000000000000000000000000001


    }
}
