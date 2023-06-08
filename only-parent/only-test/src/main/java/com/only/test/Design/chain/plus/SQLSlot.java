package com.only.test.Design.chain.plus;

public class SQLSlot extends AbstractProcessorSlot {
    @Override
    public void entry() throws Throwable {
        System.out.println("sql slot");
        fireEntry();
    }

    @Override
    public void exit(){

    }
}
