package com.only.test.mybatis;

import com.only.test.mybatis.mapper.UserMapper;
import com.only.test.mybatis.service.UserService;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringMain {

    @Test
    public void test() {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("spring.xml");

        //UserMapper userMapper = ctx.getBean(UserMapper.class);

        //User user = userMapper.getByUserId(1);

        //User user2 = userMapper.getByUserId(1);

        UserService userService = ctx.getBean(UserService.class);
        userService.getUser2(1);

        //System.out.println(user == user2);
    }

}
