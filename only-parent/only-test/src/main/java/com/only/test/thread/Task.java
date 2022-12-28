package com.only.test.thread;

public class Task implements Runnable {

    @Override
    public void run() {
        System.out.println("我的任务");
    }
}
