package com.only.test.bean;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Test {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(MainConfig.class);

        ctx.publishEvent(new ApplicationEvent("我手动发布了一个事件") {
            @Override
            public Object getSource() {
                return super.getSource();
            }
        });

        Object obj = ctx.getBean("catFactoryBean");
        System.out.println(obj);


        ctx.close();
    }

}
