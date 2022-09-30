package com.only.test.aop;

import org.springframework.aop.framework.AopContext;

public class OnlyCalculate implements Calculate {

    @Override
    public int add(int numA, int numB) {
        // AOP 不会拦截 当前对象
        //this.reduce(numA, numB);
        //Calculate currentProxy = (Calculate)AopContext.currentProxy();
        //currentProxy.reduce(numA, numB);
        System.out.println("开始执行add方法了");
        int i =1/0;
        return numA + numB;
    }

    @Override
    public int reduce(int numA, int numB) {
        return numA - numB;
    }

    @Override
    public int div(int numA, int numB) {
        return numA / numB;
    }

    @Override
    public int multi(int numA, int numB) {
        return numA * numB;
    }
}