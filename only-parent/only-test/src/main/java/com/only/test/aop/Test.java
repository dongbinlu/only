package com.only.test.aop;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Test {

    public static void main(String[] args) {

        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(MainConfig.class);

        Calculate calculate = (Calculate) ctx.getBean("calculate");
        int num = calculate.add(1, 1);
        System.out.println(num);


        ctx.close();

    }
}
