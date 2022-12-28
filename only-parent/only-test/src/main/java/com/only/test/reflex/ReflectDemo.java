package com.only.test.reflex;

import lombok.SneakyThrows;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ReflectDemo {


    @SneakyThrows
    public static void main(String[] args) {

        List list = new ArrayList<Integer>();
        list.add(1);
        list.add(2);

        Class c = Class.forName("java.util.ArrayList");
        Method method = c.getMethod("add", Object.class);

        method.invoke(list, "反射技术");


        System.out.println(list);


    }

}
