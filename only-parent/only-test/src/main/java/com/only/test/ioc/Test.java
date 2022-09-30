package com.only.test.ioc;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Test {

    public static void main(String[] args) {

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MainConfig.class);
        context.publishEvent(new ApplicationEvent("手动发布了一个事件") {

            @Override
            public Object getSource() {
                return super.getSource();
            }
        });

        Object bean = context.getBean("&pigFactoryBean");
        //System.out.println(bean);

        //Person person = (Person)context.getBean("person");


        //System.out.println(person);

        //context.close();

    }
}
