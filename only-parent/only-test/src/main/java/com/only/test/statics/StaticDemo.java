package com.only.test.statics;

public class StaticDemo {

    private int a = 10;// 实例成员
    private static int b = 20; // 类成员

    static class StaticClass {
        public static int c = 30;
        public int d = 40;

        // 类方法
        public static void print() {
            //下面代码会报错，静态内部类不能访问外部类实例成员
            //System.out.println(a);

            //静态内部类只能访问外部类类成员
            System.out.println(b);
        }

        private void syso() {

        }

        // 实例方法
        public void print1() {

        }
    }

}
