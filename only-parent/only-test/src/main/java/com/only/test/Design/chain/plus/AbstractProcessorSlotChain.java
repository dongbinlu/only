package com.only.test.Design.chain.plus;

public abstract class AbstractProcessorSlotChain extends AbstractProcessorSlot<Object> {


    public abstract void addFirst(AbstractProcessorSlot<?> protocolProcessor);


    public abstract void addLast(AbstractProcessorSlot<?> protocolProcessor);


}
