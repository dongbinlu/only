package com.only.spring.boot.autoconfigure;

public class OnlySessionTemplate implements OnlySession {


    @Override
    public Object insert() {
        System.out.println("execute insert ...");
        return "insert";
    }

    @Override
    public Object delete() {
        System.out.println("execute delete ...");
        return "delete";
    }

    @Override
    public Object update() {
        System.out.println("execute update ...");
        return "update";
    }

    @Override
    public Object query() {
        System.out.println("execute query ...");
        return "query";
    }
}
