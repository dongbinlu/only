package com.only.test.reflex;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by garfield on 2016/11/18.
 */
public class TestClass {
    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        Class c = Class.forName("com.only.test.reflex.OuterClass");
        Object o1 = c.newInstance();

        //通过方法名获取方法
        Method method = c.getDeclaredMethod("print");
        //调用外部类方法
        method.invoke(c.newInstance());
        //内部类需要使用$分隔
        Class c2 = Class.forName("com.only.test.reflex.OuterClass$InnerClass");


        Constructor declaredConstructor = c2.getDeclaredConstructor(c);
        Object o = declaredConstructor.newInstance(o1);



        Method method2 = c2.getDeclaredMethod("print2");
        //通过构造函数创建实例,如果没有重写构造方法则不管是不是获取已声明构造方法,结果是一样的
        method2.invoke(c2.getDeclaredConstructors()[0].newInstance(c.newInstance()));
    }
}