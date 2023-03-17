package com.only.test.thread;

import lombok.SneakyThrows;

public class Input implements Runnable {

    private Resource resource;

    public Input(Resource resource) {
        this.resource = resource;
    }

    @SneakyThrows
    @Override
    public void run() {
        int i = 1;
        while (true) {
            if (i % 2 == 0) {
                i = 1;
                resource.input("张三", "男");
            } else {
                i = 2;
                resource.input("小红", "女");
            }
        }

    }
}
