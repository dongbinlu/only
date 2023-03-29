package com.safecode.security.subject.test;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

/*
 * StringUtils工具常用方法(判空，转换，移除，替换，反转)
 * 注：StringUtils类在操作字符串时，即使操作的未null值也是安全的，不会包NullPointerException
 */

public class TestStringUtilsRemove {

    /*
     * StringUtils中在字符串中移除匹配的字符或字符序列，如果要移除的字符或字符序列在字符串中不存在，即无匹配，则不进行移除
     *
     * StringUtils.remove(String str , char remove)
     * StringUtils.remove(String str , String remove)
     * StringUtils.removeStart(String str , String remove)
     * StringUtils.removeStartIgnoreCase(String str , String remove)
     * StringUtils.removeEnd(String str , String remove)
     * StringUtils.removeEndIgnoreCase(String str , String remove)
     * StringUtils.deleteWhitespace(String str)
     *
     */

    @Test
    public void test() {

        System.out.println("======移除单个字符======");
        System.out.println(StringUtils.remove(null, 'a'));//null
        System.out.println(StringUtils.remove("china", null));//china
        System.out.println(StringUtils.remove("china", 'i'));//chna
        System.out.println(StringUtils.remove("china", 'b'));//china

        System.out.println("======移除指定字符序列======");
        System.out.println(StringUtils.remove("china", "chi"));//na
        System.out.println(StringUtils.remove("china", "nin"));//china

        System.out.println("======移除开头匹配的字符序列======");
        System.out.println(StringUtils.removeStart("china", "ch"));//ina
        System.out.println(StringUtils.removeStart("china", "na"));//china
        System.out.println(StringUtils.remove("china", "CHI"));//na

        System.out.println("======移除结尾匹配的字符序列======");
        System.out.println(StringUtils.removeEnd("china", "na"));//chi
        System.out.println(StringUtils.removeEndIgnoreCase("chiNa", "na"));//chi

        System.out.println("======移除空白符======");
        System.out.println(StringUtils.deleteWhitespace(null));//null
        System.out.println(StringUtils.deleteWhitespace(""));
        System.out.println(StringUtils.deleteWhitespace(" "));
        System.out.println(StringUtils.deleteWhitespace("\t"));
        System.out.println(StringUtils.deleteWhitespace("\n"));
        System.out.println(StringUtils.deleteWhitespace("\r"));
        System.err.println(" c h i\tn\ra\nx"); // c h i	n 换行a  换行x
        System.out.println(StringUtils.deleteWhitespace(" c h i\tn\ra"));//china


    }

}
