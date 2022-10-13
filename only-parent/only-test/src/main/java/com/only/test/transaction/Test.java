package com.only.test.transaction;

import com.only.test.transaction.config.MainConfig;
import com.only.test.transaction.entity.Role;
import com.only.test.transaction.entity.User;
import com.only.test.transaction.service.UserService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Test {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(MainConfig.class);

        UserService userService = ctx.getBean(UserService.class);
        User user = User.builder()
                .username("jojo")
                .build();

        Role role = Role.builder()
                .roleName("管理员")
                .build();
        userService.insert(user, role);


        ctx.close();
    }

}
