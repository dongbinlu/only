package com.only.test.algorithm.flow;

public class LeakyBucket {


    public long timeStamp = System.currentTimeMillis();// 当前时间

    public long capacity = 100;// 桶的容量

    public long rate; //水漏出的速度（每秒系统能处理的请求数）

    public long water; // 当前水量（当前累计请求数）


    public boolean limit() {

        long now = System.currentTimeMillis();

        // 先执行漏水，计算剩余水量
        water = water - ((now - timeStamp) / 1000) * rate;

        water = Math.max(0, water);


        timeStamp = now;

        if ((water + 1) < capacity) {
            water += 1;// 尝试加水，并且水还未满
            System.out.println("哈哈哈");
            return true;
        } else {
            // 水满，拒绝加水
            System.out.println("nonono");
            return false;
        }

    }

    public static void main(String[] args)throws Exception {

        LeakyBucket bucket = new LeakyBucket();

        for (int i = 0; i < 1000; i++) {
            bucket.limit();
            Thread.sleep(100);
        }
    }

}
