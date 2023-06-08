package com.only.test.Design.chain.plus;

public class ParamSlot extends AbstractProcessorSlot{
    @Override
    public void entry() throws Throwable {
        System.out.println("param slot");
        fireEntry();
    }

    @Override
    public void exit() {

    }
}
