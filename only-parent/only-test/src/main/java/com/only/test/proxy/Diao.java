package com.only.test.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class Diao implements InvocationHandler {

    private Bird bird;

    public Diao(Bird bird) {
        this.bird = bird;
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        String methodName = method.getName();

        // 进行拦截fly方法
        if ("fly".equals(methodName)) {
            System.out.println("增强了fly");
        }
        //第一个参数是一个对象。此对象可以为：①方法持有者；②方法持有者的继承者

        return method.invoke(bird, args);
    }
}
