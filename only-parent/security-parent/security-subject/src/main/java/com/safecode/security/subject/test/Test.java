package com.safecode.security.subject.test;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class Test {

    public static void main(String[] args) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(
                LocalDateTime.now().plusMonths(1).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());

        System.err.println("date :" + date);
        System.err.println("str :" + format.format(date));
        System.err.println("time :" + format.format(new Date()));

        if (new Date().after(new Date(
                LocalDateTime.now().plusSeconds(1).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()))) {
            System.err.println("true");

        }

        String string = "  ";
        String[] split = string.split(",");
        if (split.length > 1 && split.length <= 3) {
            System.err.println("--");
        }
        System.err.println(split.length);


        System.out.println("------------------");
        System.out.println("imageCode".toLowerCase());
        System.out.println(Test.class.getSimpleName());
    }

}
