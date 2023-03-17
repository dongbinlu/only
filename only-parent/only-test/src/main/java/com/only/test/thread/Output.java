package com.only.test.thread;

import lombok.SneakyThrows;

public class Output implements Runnable {
    private Resource resource;

    public Output(Resource resource) {
        this.resource = resource;
    }

    @SneakyThrows
    @Override
    public void run() {
        while (true) {
            resource.output();
        }
    }
}
