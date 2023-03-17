package com.only.test.volatiles;

public class Test {

    private volatile static int count = 0;



    public static void main(String[] args) throws InterruptedException {

        for (int i = 0; i < 10; i++) { // 创建10个线程

            Thread thread = new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    // 此操作不是一个原子操作
                    //1、load count 到工作内存
                    //2、add count 执行自加
                    synchronized (Test.class) {
                        count++;
                    }
                    // 可能还有其他业务代码，当线程1没有将数据立马写回主内存时，导致线程二延迟阻塞，CPU可能会指令重排，让count++后面的指令先执行。
                }
            });

            thread.start();
        }
        Thread.sleep(1000L);
        System.out.println(count);
    }

}
