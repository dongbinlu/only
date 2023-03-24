package com.only.test.bean;

public class B {


    private A a;


    public B() {
        System.out.println("B的构造器");
    }


    public void setA(A a) {
        this.a = a;
    }
}
