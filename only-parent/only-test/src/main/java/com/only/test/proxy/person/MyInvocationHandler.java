package com.only.test.proxy.person;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class MyInvocationHandler implements InvocationHandler {

    private Object object;

    public MyInvocationHandler(Object object) {
        this.object = object;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("MyInvocationHandler invoke begin");
        System.out.println("proxy: " + proxy.getClass().getName());
        System.out.println("method: " + method.getName());
        for (Object arg : args) {
            System.out.println("arg: " + arg);
        }
        //通过反射调用 被代理类的方法
        method.invoke(object, args);
        System.out.println("MyInvocationHandler invoke end");
        return null;
    }
}
