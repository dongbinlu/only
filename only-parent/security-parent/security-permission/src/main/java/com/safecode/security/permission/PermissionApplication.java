package com.safecode.security.permission;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.safecode.security.permission.common.JsonData;

@SpringBootApplication
@MapperScan("com.safecode.security.permission.mapper")
@RestController
public class PermissionApplication {

    public static void main(String[] args) {
        SpringApplication.run(PermissionApplication.class, args);
    }

    @GetMapping("/hello")
    public JsonData hello() {
        return JsonData.success("hello permission !!!");
    }
}
