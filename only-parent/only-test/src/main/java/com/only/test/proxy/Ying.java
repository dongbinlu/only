package com.only.test.proxy;

public class Ying implements Bird {

    @Override
    public void fly() {
        System.out.println("Ying fly 10m");
    }

    @Override
    public void eat() {
        System.out.println("Ying eat 10t");
    }

    @Override
    public void sleep() {
        System.out.println("Ying sleep 10s");
    }
}
