package com.only.test.boot.schedule;

import ch.qos.logback.core.joran.spi.NoAutoStart;
import ch.qos.logback.core.net.SyslogOutputStream;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.awt.image.VolatileImage;
import java.util.concurrent.TimeUnit;

@Component
public class Task {

    volatile int x;

    volatile int y;

    volatile int z;

    @Async
    @Scheduled(cron = "0/3 * * * * ?")
    public void task_one() {

        System.out.println("task_one:" + x++);

    }

    @Async
    @Scheduled(fixedRate = 3, timeUnit = TimeUnit.SECONDS)
    public void task_two() {

        System.out.println("task_two:" + y++);

    }

    @Async
    @Scheduled(fixedRateString = "${dms.handle.scheduled.dev.check.status}", timeUnit = TimeUnit.SECONDS)
    public void task_three() {

        System.out.println("task_three:" + z++);

    }

}
