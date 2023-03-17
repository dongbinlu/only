package com.only.test.statics;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

public class Test {

    public static void main(String[] args) {
        Node exclusive = Node.EXCLUSIVE;
        System.out.println(exclusive);
    }


    static final class Node {
        static final Node SHARED = new Node();
        static final Node EXCLUSIVE = null;
    }

}
