package com.only.test.locks;

import java.util.concurrent.locks.ReentrantLock;

public class LockTemplate {

    /**
     * 公平锁，非公平锁
     * 需要保证多个线程使用的是同一把锁
     * <p>
     * synchronized是否可重入
     * 虚拟机，在ObjectMonitor.hpp顶一个synchronized怎么去重入加锁...hotspot源码
     * counter+1
     * 基于AQS 去实现加锁与解锁
     *
     * @param threadName
     */


    /**
     * public ReentrantLock(boolean fair) {
     * sync = fair ? new FairSync() : new NonfairSync();
     * }
     * 创建一把公平锁
     */
    private ReentrantLock lock = new ReentrantLock(true);

    private volatile int counter;

    //需要保证多个线程使用的是同一个ReentrantLock对象
    public void modifyResources(String threadName) {
        System.out.println("通知（管理员）线程：--->" + threadName + "准备打水");

        //默认创建的是独占锁，排它锁：同一时刻读或者写只允许一个线程获取锁
        lock.lock();
        System.out.println("线程:--->" + threadName + "第一次加锁");
        counter++;
        System.out.println("线程：" + threadName + "打第" + counter + "桶水");

        //重入该锁，我还有一件事情要做，没做完之前不能把锁资源让出去
        //lock.lock();
        //System.out.println("线程:--->" + threadName + "第二次加锁");
        //counter++;
        //System.out.println("线程：" + threadName + "打第" + counter + "桶水");
        //
        //lock.unlock();
        //System.out.println("线程：" + threadName + "释放一个锁");
        lock.unlock();
        //System.out.println("线程：" + threadName + "释放一个锁");
    }

    public static void main(String[] args) {
        LockTemplate tp = new LockTemplate();

        new Thread(() -> {
            String name = Thread.currentThread().getName();
            tp.modifyResources(name);
        }, "杨过").start();


        new Thread(() -> {
            String name = Thread.currentThread().getName();
            tp.modifyResources(name);
        }, "小龙女").start();


    }

}
