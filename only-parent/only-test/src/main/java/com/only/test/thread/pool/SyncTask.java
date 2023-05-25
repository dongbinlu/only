package com.only.test.thread.pool;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class SyncTask implements Runnable {


    private BlockingQueue<String> tasks = new LinkedBlockingQueue<>();


    public void addTask(String name) {
        tasks.add(name);
    }

    @Override
    public void run() {
        System.out.println("开始执行了");

        while (true) {
            try {
                String name = tasks.take();
                System.out.println(name + "执行任务了");
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }
}
