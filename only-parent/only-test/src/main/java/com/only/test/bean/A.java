package com.only.test.bean;

import jdk.nashorn.internal.ir.annotations.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class A {

    private B b;

    public A() {
        System.out.println("Açæé å¨");
    }

    public B getB() {
        return b;
    }

    public void setB(B b) {
        this.b = b;
    }
}
