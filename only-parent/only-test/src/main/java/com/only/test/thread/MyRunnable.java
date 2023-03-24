package com.only.test.thread;

public class MyRunnable implements Runnable {
    @Override
    public void run() {
        //try {
            int i = 1 / 0;
        //} catch (Exception e) {
        //    System.out.println("有异常了");
        //}
        System.out.println("hello myRunnable");
    }
}
