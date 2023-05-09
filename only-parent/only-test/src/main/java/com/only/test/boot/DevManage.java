package com.only.test.boot;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@Data
public class DevManage {


    private final ThreadLocal<Integer> timeoutHolder = new ThreadLocal<>();
    int timeout = 3;

    private ExecutorService service = Executors.newFixedThreadPool(10);


    public void send() {

        service.submit(() -> {
            System.out.println("0000");
            Integer timeout = timeoutHolder.get();
            System.out.println(timeout);
        });

    }


}
