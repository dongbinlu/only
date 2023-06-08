package com.only.test.algorithm.flow;

import org.apache.commons.lang3.RandomUtils;

import java.util.LinkedList;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * 滑动时间窗口限流-生产环境
 */
public class SlidingTimeWindowBus {

    // true：限流，false：不限流
    private volatile static boolean flag = false;

    // 请求累加数
    private static long counter = 0L;

    // 限流数，1s中内，只能通过100个请求
    private final static int LIMIT = 100;

    // 使用LinkedList来记录滑动时间窗口的格子，最大为10个格子，每个格子100ms，10个格子也就1s。
    private static LinkedList<Long> slots = new LinkedList<Long>();


    private static ScheduledExecutorService executor;

    static {
        executor = new ScheduledThreadPoolExecutor(1, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setDaemon(true);
                thread.setName("com.only.algorithm.flow.SlidingTimeWindowBus");
                return thread;
            }
        });
        executor.schedule(new TimeWindow(), 0, TimeUnit.SECONDS);

    }

    static class TimeWindow implements Runnable {

        @Override
        public void run() {
            try {
                init();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void init() throws Exception {
        while (true) {
            // 记录每次循环时counter的实时数，注意：counter是累加的，当前的数包含前面的数
            slots.addLast(counter);
            // 判断格子的数量，如果大于10，需要移除掉第一个格子，保证窗口在10个以内。
            if (slots.size() > 10) {
                slots.removeFirst();
            }

            // 比较最后一个和第一个，两者相差100以上则限流
            if ((slots.getLast() - slots.getFirst()) > LIMIT) {
                flag = true;
            } else {
                flag = false;
            }
            // 每隔100ms，时间窗口向后移动一格，并判断是否限流。
            Thread.sleep(100L);
        }
    }


    public static void main(String[] args) throws Exception {


        while (true) {
            if (limit()) {
                System.out.println("被限流了");
            } else {
                System.out.println(200);
            }
            Thread.sleep(RandomUtils.nextInt(0, 17));

        }

    }

    public static boolean limit() {
        counter++;
        return flag;
    }

}
