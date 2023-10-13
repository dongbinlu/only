package com.only.test.countdownlatch;

import java.util.concurrent.CountDownLatch;

/**
 * 配媳妇去看病，轮到媳妇看大夫时
 * 我就开始去排队准备交钱了。
 */
public class CountDownLaunchSample {
    public static void main(String[] args) throws InterruptedException {
        long now = System.currentTimeMillis();
        CountDownLatch countDownLatch = new CountDownLatch(2);

        new Thread(new SeeDoctorTask(countDownLatch)).start();
        new Thread(new QueueTask(countDownLatch)).start();

        //等待线程池中的2个任务执行完毕，否则一直
        countDownLatch.await();
        System.out.println("over，回家 cost:" + (System.currentTimeMillis()));
    }
}
