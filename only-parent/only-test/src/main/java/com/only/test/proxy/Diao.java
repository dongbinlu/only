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

        return method.invoke(bird, args);
    }
}
