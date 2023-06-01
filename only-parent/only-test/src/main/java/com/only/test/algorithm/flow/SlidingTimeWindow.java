package com.only.test.algorithm.flow;

import java.util.LinkedList;

/**
 * 滑动时间窗口限流
 */
public class SlidingTimeWindow {

    public Long counter = 0L;

    public final int limit = 100;// 时间窗口内最大请求数

    LinkedList<Long> slots = new LinkedList<Long>();


    public boolean limit() {

        counter++;

        slots.addLast(counter);

        if (slots.size() > 10) {
            slots.removeFirst();
        }
        // 比较最后一个和第一个，两者相差100就限流
        return (slots.peekLast() - slots.peekFirst()) < limit ? true : false;

    }


    public static void main(String[] args) {

        SlidingTimeWindow slidingTimeWindow = new SlidingTimeWindow();

        while (true) {
            if (slidingTimeWindow.limit()) {
                System.out.println("hello");
            } else {
                System.out.println("nonono");
            }
        }

    }

}
