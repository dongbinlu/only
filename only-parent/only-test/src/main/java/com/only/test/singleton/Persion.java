package com.only.test.singleton;


public class Persion {

    private static Persion instance = null;

    private String name;

    private Persion(String name) {
        for (long i = 0; i < 100000000000L; i++) {

        }
        this.name = name;
    }


    public static Persion getInstance() {

        if (instance == null) {

            synchronized (Persion.class) {
                if (instance == null) {
                    /**
                     *此处2和3可指令重排，导致属性还没初始化完就已经将内存地址赋值给instance，这样
                     * 导致在高并发情况下，线程获取instance不为空（属性为空，还没初始化完）导致线程安全问题。
                     * 1、分配对象内存空间
                     * 2、初始化对象
                     * 3、将内存地址赋值给instance变量
                     */
                    instance = new Persion("haha");
                }
            }
        }
        return instance;
    }


    public static void main(String[] args) throws InterruptedException {
        //for (int i = 0; i < 10; i++) {

        new Thread(() -> {
            Persion instance = Persion.getInstance();
            System.out.println(instance + "--" + instance.name);
        }).start();

        Thread.sleep(2000);

        new Thread(() -> {
            Persion instance = Persion.getInstance();
            System.out.println(instance + "--" + instance.name);
        }).start();
        //}
    }

}
