package com.only.test.thread;

import lombok.SneakyThrows;

import java.util.concurrent.*;

public class Test {


    private static ThreadPoolExecutor service = new ThreadPoolExecutor(
            10,
            16,
            3L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>(),
            Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.AbortPolicy());

    private static ScheduledThreadPoolExecutor scheduled = new ScheduledThreadPoolExecutor(
            10,
            Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.AbortPolicy());

    @SneakyThrows
    public static void main(String[] args) {
        MyRunnable myRunnable = new MyRunnable();
        service.submit(myRunnable);
        //System.out.println(10 % 13);

        //scheduled.schedule(myRunnable,
        //        1,
        //        TimeUnit.SECONDS);

        //scheduled.scheduleAtFixedRate(myRunnable,
        //        1,
        //        3,
        //        TimeUnit.SECONDS);


        //scheduled.scheduleWithFixedDelay(myRunnable,
        //        1,
        //        3,
        //        TimeUnit.SECONDS);



        System.out.println(10 << 7);

    }

}
