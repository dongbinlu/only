package com.only.test.mybatis;

import com.only.test.mybatis.entity.User;
import com.only.test.mybatis.mapper.UserMapper;
import com.only.test.mybatis.service.UserService;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringMain {

    @Test
    public void test() throws Exception {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("spring.xml");
/*
        UserMapper userMapper = ctx.getBean(UserMapper.class);

        User user = userMapper.getByUserId(1);

        User user2 = userMapper.getByUserId(1);

        System.out.println(user == user2);*/


        UserService userService = ctx.getBean(UserService.class);
        userService.getUser2(1);

        /**
         * 尽管开启事务，每个线程都会创建一个属于自己的sqlsession（ThreadLocal），互相隔离
         */
        Thread thread = new Thread(() -> {
            userService.getUser2(1);
        });
        thread.start();
        thread.join();
        System.out.println("end");

    }

}
