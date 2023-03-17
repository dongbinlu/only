package com.only.test.queue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ArrayBlockingQueueTest {

    //创建容量大小为1的有界队列
    private BlockingQueue<Ball> blockingQueue = new ArrayBlockingQueue<>(1);

    //队列大小
    public int queueSize() {
        return blockingQueue.size();
    }

    //将球放入队列当中
    public void produce(Ball ball) throws InterruptedException {
        blockingQueue.put(ball);
    }

    // 将球从队列中拿出来
    public Ball consume() throws InterruptedException {
        return blockingQueue.take();
    }

    public static void main(String[] args) throws Exception {
        ArrayBlockingQueueTest box = new ArrayBlockingQueueTest();
        ExecutorService executorService = Executors.newCachedThreadPool();


        new Thread(() -> {

            Ball ball = new Ball();
            ball.setNumber("编号:" + 1);
            ball.setColor("yellow");
            try {
                box.produce(ball);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("放入乒乓球编号：" + ball.getNumber() + ",颜色：" + ball.getColor() + ",当前剩余：" + box.queueSize() + "个球");
        }, "线程1").start();

        Thread.sleep(1000);
        new Thread(() -> {

            Ball ball = new Ball();
            ball.setNumber("编号:" + 2);
            ball.setColor("yellow");
            try {
                box.produce(ball);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("放入乒乓球编号：" + ball.getNumber() + ",颜色：" + ball.getColor() + ",当前剩余：" + box.queueSize() + "个球");
        }, "线程2").start();

        Thread.sleep(1000);

        new Thread(() -> {
            Ball ball = null;
            try {
                ball = box.consume();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("拿出乒乓球编号：" + ball.getNumber() + ",颜色：" + ball.getColor() + ",当前剩余：" + box.queueSize() + "个球");

        }, "线程3").start();


        Thread.sleep(10 * 10000000);

/*
        // 往箱子里放入球
        executorService.submit(() -> {
            int i = 0;
            while (true) {
                Ball ball = new Ball();
                ball.setNumber("编号:" + i);
                ball.setColor("yellow");
                box.produce(ball);
                System.out.println("放入乒乓球编号：" + ball.getNumber() + ",颜色：" + ball.getColor() + ",当前剩余：" + box.queueSize() + "个球");
                i++;
            }
        });

        // 从箱子里拿球
        executorService.submit(() -> {
            while (true) {
                Ball ball = box.consume();
                System.out.println("拿出乒乓球编号：" + ball.getNumber() + ",颜色：" + ball.getColor() + ",当前剩余：" + box.queueSize() + "个球");
            }
        });*/
    }

}
