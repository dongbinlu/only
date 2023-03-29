package com.safecode.security.subject.test;

import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

/*
 * StringUtils工具常用方法(判空，转换，移除，替换，反转)
 * 注：StringUtils类在操作字符串时，即使操作的未null值也是安全的，不会包NullPointerException
 */

public class TestStringUtilsConvert {

    /*
     * StringUtils中涉及大小写转换以及判断字符串大小写的方法如下
     *
     * StringUtils.capitalize(String str)
     *
     * StirngUtils.uncapitalize(String str)
     *
     * StringUtils.upperCase(String str)
     * StringUtils.upperCase(String str , Locale locale)
     *
     * StringUtils.lowerCase(String str)
     * StringUtils.lowerCase(String str , Local locale)
     *
     * StringUtils.swapCase(String str)
     *
     * StringUtils.isAllUpperCase(CharSequence css)
     * StringUtils.isAllLowerCase(CahrSequence css)
     */

    @Test
    public void test() {

        // 字符串首字符大小写转换
        System.out.println("======capitalize(大写的)======");
        System.out.println(StringUtils.capitalize(null));// null (注意：此处不会报异常)
        System.out.println(StringUtils.capitalize("china"));// China
        System.out.println(StringUtils.uncapitalize(null));// null
        System.out.println(StringUtils.uncapitalize("CHINA"));// cHINA

        // 字符串整体大小写转化
        System.out.println("======字符串整体大小写转化======");
        System.out.println(StringUtils.upperCase(null));//null
        System.out.println(StringUtils.upperCase("chIna", Locale.ENGLISH));//CHINA
        System.out.println(StringUtils.lowerCase(null));//null
        System.out.println(StringUtils.lowerCase("ChINA", Locale.ENGLISH));//china

        // 字符串大小写互换
        System.out.println("======字符串大小写互换======");
        System.out.println(StringUtils.swapCase("chINa"));//CHinA

        // 判断字符串是否全部都是大写或小写(空和空白符均为false)
        System.out.println("======判断字符串是否全部都是大写或小写(空和空白符均为false)======");
        System.out.println(StringUtils.isAllUpperCase(null));//false
        System.out.println(StringUtils.isAllUpperCase(""));//false
        System.out.println(StringUtils.isAllUpperCase(" "));//false
        System.out.println(StringUtils.isAllUpperCase("CHINA"));//true
        System.out.println(StringUtils.isAllUpperCase("CHina"));//false

        System.out.println(StringUtils.isAllLowerCase(null));//false
        System.out.println(StringUtils.isAllLowerCase(""));//false
        System.out.println(StringUtils.isAllLowerCase(" "));//false
        System.out.println(StringUtils.isAllLowerCase("china"));//true
        System.out.println(StringUtils.isAllLowerCase("CHIna"));//false

    }

}
