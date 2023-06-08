package com.only.test.algorithm.flow;

import java.util.LinkedList;
import java.util.Random;

/**
 * 滑动时间窗口限流
 */
public class SlidingTimeWindow {

    public Long counter = 0L;

    public final int limit = 100;// 时间窗口内最大请求数

    LinkedList<Long> slots = new LinkedList<Long>();


    public boolean doCheck() throws Exception {

        while (true) {
            slots.addLast(counter);
            if (slots.size() > 10) {
                slots.removeFirst();
            }
            if ((slots.peekLast() - slots.peekFirst()) > limit) {
                System.out.println("限流了");
            } else {
                System.out.println("ok");
            }
            Thread.sleep(100L);
        }

    }


    public static void main(String[] args) throws Exception {

        SlidingTimeWindow slidingTimeWindow = new SlidingTimeWindow();


        new Thread(() -> {

            try {
                slidingTimeWindow.doCheck();
            } catch (Exception e) {
                e.printStackTrace();
            }


        }).start();

        // 模拟web请求
        while (true) {
            slidingTimeWindow.counter++;
            Thread.sleep(new Random().nextInt(15));
        }

    }

}
