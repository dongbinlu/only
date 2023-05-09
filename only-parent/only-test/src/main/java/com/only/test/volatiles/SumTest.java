package com.only.test.volatiles;

import lombok.SneakyThrows;

/**
 * 多线程累加
 */
public class SumTest {

    private static int sum = 0;

    public static void main(String[] args) {
        method_1();
    }


    @SneakyThrows
    private static void method_1() {

        for (int i = 0; i < 1000; i++) {

            new Thread(() -> {
                for (int j = 0; j < 10; j++) {
                    sum++;
                }
            }).start();

        }


//        Thread.sleep(2 * 1000);

        System.out.println(sum);

    }

}
