package com.only.test.Design.builder;

public class Test {

    public static void main(String[] args) {
        User build = new User.Builder("a", "b")
                .age(12)
                .phone("110")
                .build();

        System.out.println(build);
    }
}
