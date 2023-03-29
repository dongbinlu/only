package com.safecode.security.subject.test;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

/*
 * StringUtils工具常用方法(判空，转换，移除，替换，反转)
 * 注：StringUtils类在操作字符串时，即使操作的未null值也是安全的，不会包NullPointerException
 */

public class TestStringUtilsReplace {

    /**
     * StringUtils中常用的替换方法如下
     * <p>
     * 1,StringUtils.replace(String text , String searchString , String
     * replacement) 2,StringUtils.replace(String text , String searchString ,
     * String replacement , int max) 3,StringUtils.replaceChars(String str ,
     * char searchChar , char replaceChar) 4,StringUtils.replaceChars(String str
     * , String searchChars , String replaceChars)
     * 5,StringUtils.replaceOnce(String text , String searchString , String
     * replacement) 6,StringUtils.overlay(String str , String overlay , int
     * start , int end) 7,StringUtils.replaceEach(String text , String[]
     * searchList , String[] replacementList)
     * 8,StringUtils.replaceEachRepeatedly(String text , String[] searchList ,
     * String[] replacementList)
     * <p>
     * 需要注意的是，若被替换的字符串为null，或者被替换的字符或字符串序列为null,又或者替换的字符或字符序列为null，那么此次替换都会被忽略，
     * 返回原字符串
     */

    @Test
    public void test() {

        System.out.println("======替换单个字符或字符序列，replace方法======");
        System.out.println(StringUtils.replace("china", null, "z"));// china 此处被替换字符串序列为null，因此替换会被忽略，返回原字符串
        System.out.println(StringUtils.replace("china", "c", null));// china 此处替换字符序列为null，一次替换会被忽略，返回原来字符串
        System.out.println(StringUtils.replace("china", "a", "ese"));// chinese
        System.out.println(StringUtils.replace("china", "a", ""));// chin
        System.out.println(StringUtils.replace("china", "a", " ese"));// chin ese

        System.out.println("======替换单个字符或字符序列，replace方法还可以指定最大替换的个数======");
        System.out.println(StringUtils.replace("aabaaa", "a", "z", 0));// aabaaa (0表示替换的个数为0，也就是不替换)
        System.out.println(StringUtils.replace("aabaaa", "a", "z", 1));// zabaaa (1表示最多替换一个)
        System.out.println(StringUtils.replace("aabaaa", "a", "z", 2));// zzbaaa (2表示最多替换2个)
        System.out.println(StringUtils.replace("aabaaa", "a", "z", 3));// zzbzaa (3表示最多替换3个)
        System.out.println(StringUtils.replace("aabaaa", "a", "z", -1));// zzbzzz (-1表示全部替换)

        System.out.println("======替换单个字符或字符序列，replaceChars======");
        System.out.println(StringUtils.replaceChars("china", 'a', 'z'));// chinz
        System.out.println(StringUtils.replaceChars("china", "a", "z"));// chinz

        System.out.println("======replaceOnce只会替换一次，也就是只会替换第一个要替换的字符序列，后面即使有匹配的字符序列也不会被替换======");
        System.out.println(StringUtils.replace("abaa", "a", "z", 1));// zbaa
        System.out.println(StringUtils.replaceOnce("abaa", "a", "z"));// zbaa
        System.out.println(StringUtils.replaceFirst("abaa", "a", "z"));// zbaa

        System.out.println("======overlay(String str , String overlay , int start , int end)方法"
                + "可以在指定位置进行字符序列替换，从start索引处开始(包含)到end-1索引处为止进行替换======");
        System.out.println(StringUtils.overlay("abcdef", "zzzz", 2, 4));// abzzzzef

        System.out.println("======起始索引start小于结束索引end，这时会将end作为其实索引，start作为结束索引======");
        System.out.println(StringUtils.overlay("abcdef", "zzzz", 4, 2));// abzzzzef
        System.out.println(StringUtils.overlay("abcdef", "zzzz", 4, 3));// abczzzzef
        System.out.println(StringUtils.overlay("abcdef", "zzzz", 4, 4));// abcdzzzzef
        System.out.println(StringUtils.overlay("abcdef", "zzzz", 4, 5));// abcdzzzzf

        System.out.println(StringUtils.overlay("abcdef", "zzzz", -1, 2));// zzzzcdef
        System.out.println(StringUtils.overlay("abcdef", "zzzz", -2, -3));// zzzzabcdef

        System.out.println(
                "======replaceEach方法，同时替换多个字符序列，replaceEach(String text, String[] searchList , String[] replacementList"
                        + "方法可以同时替换多个字符序列，但被替换和替换的字符序列的个数应该对应，否则会报IllegalArgumentException)");
        System.out.println(StringUtils.replaceEach("china", new String[]{"ch", "a"}, new String[]{"x", "z"}));// xinz(将ch和a分别替换成x和z)
        System.out
                .println(StringUtils.replaceEach("china", new String[]{"ch", "a"}, new String[]{"x", "z", "y"}));// IllegalArgumentException

        System.out.println("======replaceEachRepeatedly(String text , String[] searchList , String[] replacementList)"
                + "方法可以循环进行替换======");
        System.out.println(
                StringUtils.replaceEachRepeatedly("china", new String[]{"c", "x"}, new String[]{"x", "z"}));// zhina(c被替换为x，x又被替换为z)
        System.out.println(
                StringUtils.replaceEachRepeatedly("china", new String[]{"c", "x"}, new String[]{"x", "c"}));// 但如果替换是一个死循环，会报IllegalStateException

    }

}
