package com.only.test.thread.pool;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;

public class TestSyncTask {

    private static volatile SyncTask syncTask = null;

    private static ScheduledExecutorService executor = null;

    static {

        syncTask = new SyncTask();

        executor = new ScheduledThreadPoolExecutor(1, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);

                t.setDaemon(true);
                t.setName("com.alibaba.nacos.naming.distro.notifier");

                return t;
            }
        });

        executor.submit(syncTask);

    }


    public static void main(String[] args) throws Exception {


        for (int i = 0; i < 10; i++) {
            syncTask.addTask("boy" + i);
            Thread.sleep(2000L);
        }

    }

}
