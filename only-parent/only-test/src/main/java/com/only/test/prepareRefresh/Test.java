package com.only.test.prepareRefresh;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Test {

    public static void main(String[] args) {

        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(MainConfig.class);

        Dept dept = ctx.getBean(Dept.class);
        System.out.println(dept);

        System.out.println("end");
        //ctx.close();

    }
}
