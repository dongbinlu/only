package com.only.test;

public class Zi extends Fu{

    @Override
    public void method2() {
        super.method2();
    }

    public void method(){


        System.out.println(b);

        method1();
    }

    public static void main(String[] args) {
        Zi zi = new Zi();
        zi.method();
    }
}
