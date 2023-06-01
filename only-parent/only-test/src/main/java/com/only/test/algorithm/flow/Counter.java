package com.only.test.algorithm.flow;

/**
 * 计数器法限流
 */
public class Counter {

    public long timeStamp = System.currentTimeMillis();

    public int reqCount = 0; // 请求计数器

    public final int limit = 100;// 时间窗口内最大请求数

    public final int interval = 1000 * 60;// 时间窗口 s

    public boolean limit() {

        long now = System.currentTimeMillis();

        if (now < timeStamp + interval) {
            //在时间窗口内
            reqCount++;
            // 判断当前时间窗口内是否超过最大请求控制数
            return reqCount <= limit;
        } else {
            timeStamp = now;
            // 超时后重置
            reqCount = 1;
            return true;
        }

    }


    public static void main(String[] args) {
        Counter counter = new Counter();
        while (true) {
            if (counter.limit()) {
                System.out.println("hello");
            } else {
                System.out.println("nonono");
            }
        }
    }

}
