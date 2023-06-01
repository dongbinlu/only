package com.only.test.bean.autowireMode;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Test {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(MainConfig.class);


        X x = ctx.getBean("x", X.class);
        System.out.println(x);

    }


}
