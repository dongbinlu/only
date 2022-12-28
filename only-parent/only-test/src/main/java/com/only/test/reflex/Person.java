package com.only.test.reflex;

import lombok.Data;

@Data
public class Person {

    private String name;

    private int age;

    private String address;

    public Person() {
        System.out.println("空参数构造方法");
    }

    public Person(String name) {
        this.name = name;
        System.out.println("带有String的构造方法");
    }

    private Person(String name, int age) {
        this.name = name;
        this.age = age;
        System.out.println("带有String,int的私有构造方法");
    }

    public Person(String name, int age, String address) {
        this.name = name;
        this.age = age;
        this.address = address;
        System.out.println("带有String,int,String的构造方法");
    }

    public void method1() {
        System.out.println("没有返回值没有参数的方法");
    }

    public void method2(String name) {
        System.out.println("没有返回值,有参数的方法name=" + name);
    }

    public int method3() {
        System.out.println("有返回值，没有参数的方法");
        return 123;
    }

    public String method4(String name) {
        System.out.println("有返回值，有参数的方法");
        return name;
    }

    private void method5() {
        System.out.println("私有方法");
    }


}
