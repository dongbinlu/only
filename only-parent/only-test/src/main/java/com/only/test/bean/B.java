package com.only.test.bean;

import org.springframework.stereotype.Component;

@Component
public class B {


    private A a;


    public B() {
        System.out.println("B的构造器");
    }

    public A getA() {
        return a;
    }

    public void setA(A a) {
        this.a = a;
    }
}
