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
        this.userId = 1;
        this.username = "jojo";
        this.age = 18;
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

    public Integer getUserId() {
        System.out.println("执行了getUserId()方法");
        return userId;
    }

    public void setUserId(Integer userId) {
        System.out.println("执行了setUserId()方法");
        this.userId = userId;
    }

    public String getUsername() {
        System.out.println("执行了getUsername()方法");
        return username;
    }

    public void setUsername(String username) {
        System.out.println("执行了setUsername()方法");
        this.username = username;
    }

    public Integer getAge() {
        System.out.println("执行了getAge()方法");
        return age;
    }

    public void setAge(Integer age) {
        System.out.println("执行了setAge()方法");
        this.age = age;
    }
}
