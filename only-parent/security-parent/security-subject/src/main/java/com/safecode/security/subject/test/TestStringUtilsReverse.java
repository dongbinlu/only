package com.safecode.security.subject.test;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

/*
 * StringUtils工具常用方法(判空，转换，移除，替换，反转)
 * 注：StringUtils类在操作字符串时，即使操作的未null值也是安全的，不会包NullPointerException
 */

public class TestStringUtilsReverse {

    /*
     * StringUtils中有关反转的方法如下
     *
     * 1,StringUtils.reverse(String text)
     * 2,StringUtils.reverseDelimited(String text , char separatorChar)
     */

    @Test
    public void test() {

        System.out.println("======reverse方法======");
        System.out.println(StringUtils.reverse("china"));//anihc

        System.out.println("======根据指定分隔符进行反转，分隔符之间的字符不进行反转======");
        System.out.println(StringUtils.reverseDelimited("china", ','));//china
        System.out.println(StringUtils.reverseDelimited("cxhinxa", 'x'));//axhinxc
        System.out.println(StringUtils.reverseDelimited("c.hin.a", '.'));//a.hin.c
        System.out.println(StringUtils.reverseDelimited("c.hina", '.'));//hina.c
        System.out.println(StringUtils.reverseDelimited(".china", '.'));//china
        System.out.println(StringUtils.reverseDelimited("china.", '.'));//china
    }

}
