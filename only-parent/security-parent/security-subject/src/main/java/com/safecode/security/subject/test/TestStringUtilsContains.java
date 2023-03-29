package com.safecode.security.subject.test;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

/*
 * StringUtils工具常用方法(截取，去除空白，包含，查询索引)
 * 注：StringUtils类在操作字符串时，即使操作的未null值也是安全的，不会包NullPointerException
 */

public class TestStringUtilsContains {

    /*
     * StringUtils中判断是否包含的方法主要有
     * 1,StringUtils.contains(CharSequence seq, int searchChar)
     * 2,StringUtils.contains(CharSequence seq, CharSequence searchSeq)
     * 3,StringUtils.containsIgnoreCase(CharSequence str, CharSequence searchStr)
     * 4,StringUtils.containsAny(CharSequence cs, char... searchChars)
     * 5,StringUtils.containsAny(CharSequence cs, CharSequence searchChars)
     * 6,StringUtils.containsOnly(CharSequence cs,char… valid)
     * 7,StringUtils.containsOnly(CharSequence cs, String validChars)
     * 8,StringUtils.containsNone(CharSequence cs,char… searchChars)
     * 9,StringUtils.containsNone(CharSequence cs, String invalidChars)
     * 10,StringUtils.startsWith(CharSequence str,CharSequence prefix)
     * 11,StringUtils.startsWithIgnoreCase(CharSequence str,CharSequence prefix)
     * 12,StringUtils.startsWithAny(CharSequence string,CharSequence… searchStrings)
     */

    @Test
    public void test() {

        /**
         * 判断字符串中是否包含指定的字符或字符序列
         */
        System.out.println("======区分大小写======");
        System.out.println(StringUtils.contains(null, 'a')); // false
        System.out.println(StringUtils.contains("china", null)); // false
        System.out.println(StringUtils.contains("", 'a')); // false
        System.out.println(StringUtils.contains("china", 'a'));// true
        System.out.println(StringUtils.contains("china", 'z'));//false
        System.out.println(StringUtils.contains(null, "a")); // false
        System.out.println(StringUtils.contains("china", null)); // false
        System.out.println(StringUtils.contains("", "")); // true
        System.out.println(StringUtils.contains("abc", ""));// true
        System.out.println(StringUtils.contains("china", "na"));// true
        System.out.println(StringUtils.contains("abc", "z")); // false

        System.out.println("======不区分大小写======");
        System.out.println(StringUtils.containsIgnoreCase("china", "a"));// true
        System.out.println(StringUtils.containsIgnoreCase("china", "A"));// true
        System.out.println(StringUtils.containsIgnoreCase("china", "Z"));//false
        System.out.println(StringUtils.containsIgnoreCase(null, "A")); // false
        System.out.println(StringUtils.containsIgnoreCase("china", null)); // false
        System.out.println(StringUtils.containsIgnoreCase("", "")); // true
        System.out.println(StringUtils.containsIgnoreCase("abc", ""));// true
        System.out.println(StringUtils.containsIgnoreCase("china", "na"));// true
        System.out.println(StringUtils.containsIgnoreCase("china", "Na"));// true
        System.out.println(StringUtils.containsIgnoreCase("abc", "Z")); // false

        System.out.println("======判断字符串中是否包含指定字符集合中或指定字符串中任一字符，区分大小写======");
        System.out.println(StringUtils.containsAny(null, 'a', 'b'));// false
        System.out.println(StringUtils.containsAny("", 'a', 'b'));// false
        System.out.println(StringUtils.containsAny("abc", 'a', 'z'));// true
        System.out.println(StringUtils.containsAny("abc", 'x', 'y'));// false
        System.out.println(StringUtils.containsAny("abc", 'A', 'z'));// false
        System.out.println(StringUtils.containsAny(null, "a"));// false
        System.out.println(StringUtils.containsAny("", "a"));// false
        System.out.println(StringUtils.containsAny("abc", "ab"));// true
        System.out.println(StringUtils.containsAny("abc", "ax"));// true
        System.out.println(StringUtils.containsAny("abc", "xy"));// false
        System.out.println(StringUtils.containsAny("abc", "Ax"));// false

        System.out.println("======判断字符串中是否不包含指定的字符或指定的字符串中的字符，区分大小写：======");
        System.out.println(StringUtils.containsNone(null, 'a')); // true
        System.out.println(StringUtils.containsNone("", 'a')); // true 注意这里，空串总是返回true
        System.out.println(StringUtils.containsNone("china", ' ')); // true 注意包含空白符为true
        System.out.println(StringUtils.containsNone("china", '\t')); // true
        System.out.println(StringUtils.containsNone("china", '\r')); // true
        System.out.println(StringUtils.containsNone("china", 'x', 'y', 'z')); // true
        System.out.println(StringUtils.containsNone("china", 'c', 'y', 'z')); // false
        System.out.println(StringUtils.containsNone("china", 'C', 'y', 'z')); // true
        System.out.println(StringUtils.containsNone(null, "a")); // true
        System.out.println(StringUtils.containsNone("", "a")); // true
        System.out.println(StringUtils.containsNone("china", "")); // true
        System.out.println(StringUtils.containsNone("china", "xyz")); // true
        System.out.println(StringUtils.containsNone("china", "cyz")); // false
        System.out.println(StringUtils.containsNone("china", "Cyz")); // true

        System.out.println("======判断字符串中的字符是否都是出自所指定的字符数组或字符串，区分大小写：======");
        System.out.println(StringUtils.containsOnly(null, 'a'));// false
        System.out.println(StringUtils.containsOnly("", "a"));// true
        System.out.println(StringUtils.containsOnly("ab", ' '));// false
        System.out.println(StringUtils.containsOnly("abab", 'a', 'b', 'c'));// true
        System.out.println(StringUtils.containsOnly("abcd", 'a', 'b', 'c'));// false
        System.out.println(StringUtils.containsOnly("Abab", 'a', 'b', 'c'));// false
        System.out.println(StringUtils.containsOnly(null, "a"));// false
        System.out.println(StringUtils.containsOnly("", "a")); // true
        System.out.println(StringUtils.containsOnly("abab", "abc"));// true
        System.out.println(StringUtils.containsOnly("abcd", "abc")); // false
        System.out.println(StringUtils.containsOnly("Abab", "abc"));// false

        System.out.println("======判断字符串是否以指定的字符序列开头：======");
        System.out.println("======区分大小写：：======");
        System.out.println(StringUtils.startsWith(null, null)); // true
        System.out.println(StringUtils.startsWith(null, "abc")); // false
        System.out.println(StringUtils.startsWith("abcdef", null)); // false
        System.out.println(StringUtils.startsWith("abcdef", "abc")); // true
        System.out.println(StringUtils.startsWith("ABCDEF", "abc")); // false

        System.out.println("======不区分大小写：：======");
        System.out.println(StringUtils.startsWithIgnoreCase(null, null));// true
        System.out.println(StringUtils.startsWithIgnoreCase(null, "abc"));// false
        System.out.println(StringUtils.startsWithIgnoreCase("abcdef", null));// false
        System.out.println(StringUtils.startsWithIgnoreCase("abcdef", "abc"));// true
        System.out.println(StringUtils.startsWithIgnoreCase("ABCDEF", "abc"));// true

        System.out.println("======判断字符串是否以指定的字符序列数组中任意一个开头，区分大小写：======");
        System.out.println(StringUtils.startsWithAny(null, null));// false
        System.out.println(StringUtils.startsWithAny(null, new String[]{"abc"}));// false
        System.out.println(StringUtils.startsWithAny("abcxyz", null));// false
        System.out.println(StringUtils.startsWithAny("abcxyz", new String[]{""}));// true
        System.out.println(StringUtils.startsWithAny("abcxyz", new String[]{"abc"}));// true
        System.out.println(StringUtils.startsWithAny("abcxyz", new String[]{null, "xyz", "abc"}));// true
        System.out.println(StringUtils.startsWithAny("abcxyz", null, "xyz", "ABCX"));// false
        System.out.println(StringUtils.startsWithAny("ABCXYZ", null, "xyz", "abc"));// false
    }

}
