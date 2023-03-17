package com.only.thread.pool;

import com.sun.xml.internal.ws.policy.PolicyException;

public interface OnlyExecutorService {

    void execute(Runnable task) throws PolicyException;

    Runnable getTask();

    void shutdown();


}
