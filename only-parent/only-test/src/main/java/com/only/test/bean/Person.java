package com.only.test.bean;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public class Person implements InitializingBean, DisposableBean {

    private Integer userId;

    private String username;

    private Integer age;

    public Person() {
        System.out.println("person 的无参构造方法");
    }

    private void initMethod() {
        System.out.println("person 的initMethod方法");
    }

    private void destroyMethod() {
        System.out.println("person 的destroyMethod方法");
    }


    @Override
    public void destroy() throws Exception {
        System.out.println("DisposableBean的destroy方法 ");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("InitializingBean的afterPropertiesSet方法");
    }

    @PostConstruct
    public void init() {
        System.out.println("JSR250的PostConstruct标志的方法");
    }

    @PreDestroy
    public void destory() {
        System.out.println("JSR250的PreDestory标注的方法");
    }
}
