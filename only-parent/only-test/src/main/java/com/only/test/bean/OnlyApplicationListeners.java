package com.only.test.bean;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class OnlyApplicationListeners implements ApplicationListener {

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        System.out.println("OnlyApplicationListeners接受到了一个事件：" + event);
    }
}
