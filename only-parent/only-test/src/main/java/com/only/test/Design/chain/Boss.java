package com.only.test.Design.chain;

public class Boss extends Handler {

    public Boss(String name) {
        super(name);
    }


    @Override
    public void handle(double price) {
        if (price > 200) {
            System.out.println(name + "～处理了");
        }
    }
}
