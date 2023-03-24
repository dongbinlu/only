package com.only.test.bean;

public class A {

    public B b;

    public A(B b) {
        this.b = b;
        System.out.println("A的构造器");
    }


    //public void setB(B b) {
    //    this.b = b;
    //}
}
