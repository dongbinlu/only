package com.safecode.security.subject.test;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

/*
 * StringUtils工具常用方法(截取，去除空白，包含，查询索引)
 * 注：StringUtils类在操作字符串时，即使操作的未null值也是安全的，不会包NullPointerException
 */

public class TestStringUtilsTrim {

    /*
     * StringUtils中常用去除空白的方法如下
     * 1,StringUtils.trim(String str)
     * 2,StringUtils.trimToEmpty(String str)
     * 3,StringUtils.trimToNull(String str)
     * 4,StringUtils.strip(String str)
     * 5,StringUtils.stripToEmpty(String str)
     * 6,StringUtils.stripToNull(String str)
     * 7,StringUtils.deleteWhitespace(String str)
     */

    @Test
    public void test() {

        /**
         * trim(String str)如果被去除的字符串为null或"",则返回null或""
         */
        System.out.println("======去除字符串首尾的空白符======");
        System.out.println(StringUtils.trim(null));//null
        System.out.println(StringUtils.trim(""));//""
        System.out.println(StringUtils.trim("	"));//""
        System.out.println(StringUtils.trim("abc"));//abc
        System.out.println(StringUtils.trim(" abc "));//abc
        System.out.println(StringUtils.trim(" a b c "));//a b c 注意字符串内部的空白符是不去除的

        /*
         * 如果被去除的字符串为null或"",则都返回""
         */
        System.out.println("======trimToEmpty(String str)======");
        System.out.println(StringUtils.trimToEmpty(null));//""
        System.out.println(StringUtils.trimToEmpty(""));//""
        System.out.println(StringUtils.trimToEmpty("	"));//""
        System.out.println(StringUtils.trimToEmpty("abc"));//abc
        System.out.println(StringUtils.trimToEmpty(" abc "));//abc
        System.out.println(StringUtils.trimToEmpty(" a b c "));//a b c 注意字符串内部的空白符是不去除的

        /*
         * 如果被去除的字符串为null或"",则都返回null
         */
        System.out.println("======trimToNull(String str)======");
        System.out.println(StringUtils.trimToNull(null));//""
        System.out.println(StringUtils.trimToNull(""));//""
        System.out.println(StringUtils.trimToNull("	"));//""
        System.out.println(StringUtils.trimToNull("abc"));//abc
        System.out.println(StringUtils.trimToNull(" \t\r\nabc "));//abc
        System.out.println(StringUtils.trimToNull(" a b c "));//a b c 注意字符串内部的空白符是不去除的

        System.out.println("======deleteWhitespace(String str)去除字符串中的所有空白符======");
        System.out.println(StringUtils.deleteWhitespace(null));//null
        System.out.println(StringUtils.deleteWhitespace(""));//""
        System.out.println(StringUtils.deleteWhitespace("	"));//""
        System.out.println(StringUtils.deleteWhitespace("abc"));//abc
        System.out.println(StringUtils.deleteWhitespace(" \t\r\nabc "));//abc
        System.out.println(StringUtils.deleteWhitespace(" a b c "));//abc 注意字符串内部的空白符也去除

    }

}
