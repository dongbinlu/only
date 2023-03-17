package com.only.test.thread;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.concurrent.Callable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task implements Callable<Integer> {

    private int x;

    private int y;

    @Override
    public Integer call() throws Exception {
        return x + y;
    }
}
