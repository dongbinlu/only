package com.only.test.thread.pool;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class BeatReactor {

    public static final int DEFAULT_CLIENT_BEAT_THREAD_COUNT = Runtime.getRuntime().availableProcessors() > 1 ? Runtime.getRuntime().availableProcessors() / 2 : 1;

    private ScheduledExecutorService executorService;

    public BeatReactor() {


        executorService = new ScheduledThreadPoolExecutor(DEFAULT_CLIENT_BEAT_THREAD_COUNT, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setDaemon(true);
                thread.setName("com.only.beat");
                return thread;
            }
        });

    }

    public void addBeatInfo(String name) {
        executorService.schedule(new BeatTask(name), 0, TimeUnit.SECONDS);
    }


    class BeatTask implements Runnable {

        private String name;

        public BeatTask(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            System.out.println(name + "执行了" + "threadName: " + Thread.currentThread().getName());


            executorService.schedule(new BeatTask(name), 5, TimeUnit.SECONDS);

        }
    }


}
