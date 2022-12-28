package com.only.test.thread;

import lombok.SneakyThrows;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Test {


    private static ExecutorService service = new ThreadPoolExecutor(5, 10, 3L, TimeUnit.MINUTES,
            new LinkedBlockingQueue<Runnable>());


    @SneakyThrows
    public static void main(String[] args) {

        Task task = new Task();
        //service.execute(task);
        //
        //Thread.sleep(1 * 1000L);

        ThreadPool threadPool = new ThreadPool();

        threadPool.execute(task);

    }

}
