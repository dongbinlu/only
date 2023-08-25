package com.only.test.thread;

import java.util.concurrent.*;

public class Test {


    private static ThreadPoolExecutor service = new ThreadPoolExecutor(
            1,
            1,
            0L, TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>(1),
            Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.AbortPolicy());

    private static ScheduledThreadPoolExecutor scheduled = new ScheduledThreadPoolExecutor(
            10,
            Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.AbortPolicy());

    //@SneakyThrows
    public static void main(String[] args) {

        Task task = new Task(1, 1);

        Future<Integer> future = service.submit(task);

        try {
            Integer result = future.get(3, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } finally {
            future.cancel(false);
        }


        //MyRunnable myRunnable = new MyRunnable();
        //service.submit(myRunnable);
        //service.execute(myRunnable);
        //System.out.println(10 % 13);

        //scheduled.schedule(myRunnable,
        //        1,
        //        TimeUnit.SECONDS);

       /* ScheduledFuture<?> scheduledFuture = scheduled.scheduleAtFixedRate(myRunnable,
                1,
                3,
                TimeUnit.SECONDS);

        try {
            Object o = scheduledFuture.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
*/

        //scheduled.scheduleWithFixedDelay(myRunnable,
        //        1,
        //        3,
        //        TimeUnit.SECONDS);


        //System.out.println(10 << 7);

    }

}
