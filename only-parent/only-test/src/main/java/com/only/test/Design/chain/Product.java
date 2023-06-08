package com.only.test.Design.chain;

public class Product extends Handler {


    public Product(String name) {
        super(name);
    }


    @Override
    public void handle(double price) {

        if (price <= 100) {
            System.out.println(name + "～处理了");
        } else {
            handler.handle(price);
        }
    }
}
