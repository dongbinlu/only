package com.safecode.security.subject.test;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

/*
 * StringUtils工具常用方法(判空，转换，移除，替换，反转)
 * 注：StringUtils类在操作字符串时，即使操作的未null值也是安全的，不会包NullPointerException
 */

public class TestStringUtilsNull {

    /*
     * 判空 StringUtils中判断字符串是否为空的方法主要有以下几个
     *
     * isBlank , isNotBlank , isEmpty , isNotEmpty是判断单个字符串是否为空
     *
     * boolean StringUtils.isBlank(String str)
     * boolean StringUtils.isNotBlank(String str)
     *
     * boolean StringUtils.isEmpty(String str)
     * boolean StringUtils.isNotEmpty(String str)
     *
     * boolean StringUtils.isWhitespace(CharSequence...css)
     *
     * isAnyBlank , isNoneBlank , isAnyEmpty , isNoneEmpty 四个方法是判断多个字符串是否为空
     * isAnyBlank和isAnyEmpty,只要有一个字符串为空，结果即为true
     * isNoneBlank,isNoneEmpty,只要存在一个字符串为空，结果即为false
     *
     * boolean StringUtils.isAnyBlank(CharSequence...css)
     * boolean StringUtils.isNoneBland(CharSequence...css)
     *
     * boolean StringUtils.isAnyEmpty(CharSequence...css)
     * boolean StringUtils.isNoneEmpty(CharSequence...css)
     *
     */

    @Test
    public void test() {

        System.out.println("======blank======");
        System.out.println(StringUtils.isBlank(""));// true
        System.out.println(StringUtils.isBlank(" "));// true
        System.out.println(StringUtils.isBlank("	"));// true
        System.out.println(StringUtils.isBlank("\t"));// true
        System.out.println(StringUtils.isBlank("\r"));// true
        System.out.println(StringUtils.isBlank("\n"));// true
        System.out.println(StringUtils.isBlank(null));// true

        System.out.println("======empty======");
        System.out.println(StringUtils.isEmpty(""));// true
        System.out.println(StringUtils.isEmpty(" "));// false
        System.out.println(StringUtils.isEmpty("	"));// false
        System.out.println(StringUtils.isEmpty("\t"));// false
        System.out.println(StringUtils.isEmpty("\r"));// fasle
        System.out.println(StringUtils.isEmpty("\n"));// false
        System.out.println(StringUtils.isEmpty(null));// true

        System.out.println("======whitespace======");
        System.out.println(StringUtils.isWhitespace(""));// true
        System.out.println(StringUtils.isWhitespace(" "));// true
        System.out.println(StringUtils.isWhitespace("	"));// true
        System.out.println(StringUtils.isWhitespace("\t"));// true
        System.out.println(StringUtils.isWhitespace("\r"));// true
        System.out.println(StringUtils.isWhitespace("\n"));// true
        System.out.println(StringUtils.isWhitespace(null));// false

        /*
         * 从上面的结果可以看出： blank:代表的是空串(""),空白符(空格"","	",制表符("\t"),回车符("\r"),"\n"等)以及null值
         * empty:代表的是空串("")和null值，不包含空白符
         * whitespace:包含空串("")和空白符,不包含null
         */

        System.out.println("======anyBlak======");
        System.out.println(StringUtils.isAnyBlank("jojo", "jack", "rose"));// false
        System.out.println(StringUtils.isAnyBlank("", "jack", "rose"));// true
        System.out.println(StringUtils.isAnyBlank(" ", "jack", "rose"));// true
        System.out.println(StringUtils.isAnyBlank(null, "jack", "rose"));// true
        System.out.println(StringUtils.isAnyBlank("\t", "jack", "rose"));// true

        System.out.println("======anyEmpty======");
        System.out.println(StringUtils.isAnyEmpty("jojo", "jack", "rose"));// false
        System.out.println(StringUtils.isAnyEmpty("", "jack", "rose"));// true
        System.out.println(StringUtils.isAnyEmpty(" ", "jack", "rose"));// false
        System.out.println(StringUtils.isAnyEmpty(null, "jack", "rose"));// true
        System.out.println(StringUtils.isAnyEmpty("\t", "jack", "rose"));// false

        System.out.println("======noneBlank======");
        System.out.println(StringUtils.isNoneBlank("jojo", "jack", "rose"));// true
        System.out.println(StringUtils.isNoneBlank("", "jack", "rose"));// false
        System.out.println(StringUtils.isNoneBlank(" ", "jack", "rose"));// false
        System.out.println(StringUtils.isNoneBlank(null, "jack", "rose"));// false
        System.out.println(StringUtils.isNoneBlank("\t", "jack", "rose"));// false

        System.out.println("======noneEmpty======");
        System.out.println(StringUtils.isNoneEmpty("jojo", "jack", "rose"));// true
        System.out.println(StringUtils.isNoneEmpty("", "jack", "rose"));// false
        System.out.println(StringUtils.isNoneEmpty(" ", "jack", "rose"));// true
        System.out.println(StringUtils.isNoneEmpty(null, "jack", "rose"));// false
        System.out.println(StringUtils.isNoneEmpty("\t", "jack", "rose"));// true

    }

}
