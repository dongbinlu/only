package com.only.test.Design.chain.plus;

public class SlotChainProvider {

    public static DefaultProcessorSlotChain newSlotChain() {
        DefaultProcessorSlotChain chain = new DefaultProcessorSlotChain();
        chain.addLast(new ParamSlot());
        chain.addLast(new SQLSlot());
        chain.addLast(new ErrorSlot());
        return chain;
    }

}
