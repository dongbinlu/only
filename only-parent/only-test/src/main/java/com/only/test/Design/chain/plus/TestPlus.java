package com.only.test.Design.chain.plus;

public class TestPlus {


    public static void main(String[] args) throws Throwable {
        DefaultProcessorSlotChain chain = SlotChainProvider.newSlotChain();
        chain.entry();

        System.out.println("结束");
    }

}
