package com.only.test.volatiles;

import lombok.SneakyThrows;

public class VolatileReOrder {

    private static int x = 0, y = 0;
    private static int a = 0, b = 0;
    static Object object = new Object();

    @SneakyThrows
    public static void main(String[] args) {

        int i = 0;
        for (; ; ) {
            i++;
            x = 0;
            y = 0;
            a = 0;
            b = 0;
            Thread t1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    //由于t1先启动，先让t1等一会t2;
                    shortWait(10000);
                    a = 1;
                    //可能发生指令重排
                    x = b;
                }
            });

            Thread t2 = new Thread(new Runnable() {
                @Override
                public void run() {
                    b = 1;
                    //可能发生指令重排
                    y = a;
                }
            });
            t1.start();
            t2.start();

            t1.join();
            t2.join();

            String result = "第" + i + "次 (" + x + "," + y + ")";

            if (x == 0 && y == 0) {
                System.err.println(result);
                break;
            } else {
                System.out.println(result);
            }

        }

    }

    private static void shortWait(long interval) {
        long start = System.nanoTime();
        long end;
        do {
            end = System.nanoTime();
        } while (start + interval >= end);
    }

}

/**
 * 预计执行结果
 * x,y
 * 1，1
 * 0，1
 * 1，0
 * 0,0 CPU或jit对代码进行了指令重排
 */
