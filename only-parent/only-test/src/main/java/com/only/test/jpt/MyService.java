package com.only.test.jpt;

import org.springframework.stereotype.Service;

@Service
public class MyService {

    @MyAnnotation(expressions = {"#param1", "#param2"})
    public void myMethod(String param1, int param2) {
        System.out.println("myMethod()");
    }
}
