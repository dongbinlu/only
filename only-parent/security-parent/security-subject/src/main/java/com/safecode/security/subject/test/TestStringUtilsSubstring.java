package com.safecode.security.subject.test;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

/*
 * StringUtils工具常用方法(截取，去除空白，包含，查询索引)
 * 注：StringUtils类在操作字符串时，即使操作的未null值也是安全的，不会包NullPointerException
 */

public class TestStringUtilsSubstring {

    /*
     * StringUtils中常用截取字符串的方法如下
     * 1,StringUtils.substring(String str , int start)
     * 2,StringUtils.substring(String str , int start , int end)
     *
     * 3,StringUtils.substringAfter(String str , String separator)
     * 4,StringUtils.substringAfterLast(String str , string separator)
     *
     * 5,StringUtils.substringBefore(String str , String separator)
     * 6,StringUtils.substringBeforeLast(String str , String separator)
     *
     * 7,StringUtils.substringBetween(String str , String tag)
     */

    @Test
    public void test() {

        /*
         * 注意：截取字符串时，若被截取的字符串为null或"",则截取之后返回的只服从也为null和""
         * 1,根据指定位置截取字符串，当指定的截取位置为非负数时，则从左往右开始截取，第一位为0，后面依次类推，
         * 但当索引值为负数时，则从右往左截取，注意此时右侧第一位为-1
         */

        System.out.println("======substring方法,只指定起始位置，则截取至字符串末尾======");
        System.out.println(StringUtils.substring(null, 2));//null null和""截取后都返回null和""
        System.out.println(StringUtils.substring("", 2));//
        System.out.println(StringUtils.substring("china", 0));//china 指定的起始截取位置为0，则从第一位开始截取，也就是不截取
        System.out.println(StringUtils.substring("china", 2));//ina 指定的截取位置为2，则从左往右第三位开始截取
        System.out.println(StringUtils.substring("china", -2));//na 指定的截取位置为-2，则从右往左第二位开始截取

        System.out.println("======substring方法,指定了起始位置和结束位置，则从起始位置开始截取到结束位置（但不包含结束位置）======");
        System.out.println(StringUtils.substring(null, 2, 4)); // null null和""截取后都返回null和""
        System.out.println(StringUtils.substring("", 2, 4)); //
        System.out.println(StringUtils.substring("china", 0, 0)); //
        System.out.println(StringUtils.substring("china", 2, 4)); // in
        System.out.println(StringUtils.substring("china", -2, -4)); //
        System.out.println(StringUtils.substring("china", 2, -3)); //
        System.out.println(StringUtils.substring("china", 2, -1)); // in

        System.out.println("======substringAfter根据指定的分隔符进行截取(不包含分隔符)======");
        System.out.println("======substringAfter从分隔符第一次出现的位置向后截取======");
        System.out.println(StringUtils.substringAfter("china", "i")); // na 从第一次出现"i"的位置向后截取，不包含第一次出现的"i"
        System.out.println(StringUtils.substringAfter("china", "hi")); // na
        System.out.println(StringUtils.substringAfter("chinachina", "h")); // inachina
        System.out.println(StringUtils.substringAfter("china", "a")); // ""
        System.out.println(StringUtils.substringAfter("china", "d")); // "" 分隔符在要截取的字符串中不存在，则返回""
        System.out.println(StringUtils.substringAfter("china", "")); // china 分隔符为""，则返回原字符串
        System.out.println(StringUtils.substringAfter("china", null)); // "" 分隔符为null，则返回""


        System.out.println("======从分隔符最后一次出现的位置向后截取======");
        System.out.println(StringUtils.substringAfterLast("china", "i")); // na
        System.out.println(StringUtils.substringAfterLast("chinachina", "i")); // na "i"最后出现的位置向后截取


        System.out.println("======从分隔符第一次出现的位置向前截取：======");
        System.out.println(StringUtils.substringBefore("china", "i")); // ch
        System.out.println(StringUtils.substringBefore("chinachina", "i")); // ch 从"i"第一次出现的位置向前截取

        System.out.println("======从分隔符最后一次出现的位置向前截取：======");
        System.out.println(StringUtils.substringBeforeLast("china", "i"));
        System.out.println(StringUtils.substringBeforeLast("chinachina", "i")); // chinach

        System.out.println("======截取指定标记字符串之间的字符序列：======");
        System.out.println(StringUtils.substringBetween(null, "ch")); // null
        System.out.println(StringUtils.substringBetween("", "")); // ""
        System.out.println(StringUtils.substringBetween("tagabctag", "")); // "" 标记字符串为""，则截取后返回""
        System.out.println(StringUtils.substringBetween("", "tag")); // null // 注意此处返回的是null
        System.out.println(StringUtils.substringBetween("tagabctag", null)); // null 标记字符串为null，则截取后返回null
        System.out.println(StringUtils.substringBetween("tagabctag", "tag")); // "abc"

    }

}
