package com.only.test.proxy;

import java.lang.reflect.Proxy;

public class OnlyProxy {


    public static void main(String[] args) {

        Bird bird = (Bird) Proxy.newProxyInstance(Bird.class.getClassLoader(),
                new Class[]{Bird.class},
                new Diao(new Ying()));

        bird.fly();

        bird.eat();

        bird.sleep();

    }


}
