package com.only.test.thread.non;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Worker implements Runnable {

    private Thread thread;

    public Worker() {
        this.thread = new Thread(this);
    }


    @Override
    public void run() {
        runWorker(this);
    }

    private void runWorker(Worker worker) {
        System.out.println("hahha");
    }
}
