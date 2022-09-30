package com.only.test.ioc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Dog {

    public Dog() {
        System.out.println("dog 无参构造");
    }

    private int age;

    private String name;

}
