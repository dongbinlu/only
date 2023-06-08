package com.only.test.Design.chain.plus;

public class ErrorSlot extends AbstractProcessorSlot{
    @Override
    public void entry() throws Throwable {
        System.out.println("error slot");
        fireEntry();
    }

    @Override
    public void exit() {

    }
}
