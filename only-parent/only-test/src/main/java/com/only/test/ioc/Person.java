package com.only.test.ioc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Person {

    public Person() {
        System.out.println("person 无参构造");
    }

    private int age;

    private String name;

}
