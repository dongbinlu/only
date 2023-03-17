package com.only.test.thread.non;

public class TestWorker {

    public static void main(String[] args) {
        Worker worker = new Worker();
        Thread thread = worker.getThread();
        thread.run();
    }

}
