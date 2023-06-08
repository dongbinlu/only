package com.only.test.Design.chain;

public class Sales extends Handler {


    public Sales(String name) {
        super(name);
    }


    @Override
    public void handle(double price) {
        if (price > 100 && price <= 200) {
            System.out.println(name + "～处理了");
        } else {
            handler.handle(price);
        }
    }
}
