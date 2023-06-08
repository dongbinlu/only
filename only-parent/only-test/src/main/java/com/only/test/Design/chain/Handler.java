package com.only.test.Design.chain;

public abstract class Handler {


    public String name;

    protected Handler handler;

    public Handler(String name) {
        this.name = name;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }


    /**
     * 处理业务
     */
    public abstract void handle(double price);


}
