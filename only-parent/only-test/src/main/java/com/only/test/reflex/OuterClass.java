package com.only.test.reflex;

/**
 * Created by garfield on 2016/11/18.S
 */
public class OuterClass {
    public void print(){
        System.out.println("i am Outer class");
    }
     class InnerClass{
         InnerClass(){
             
         }
         void print2(){
            System.out.println("i am inner class");
        }
    }
}