package com.only.test.Design.chain.plus;

public class DefaultProcessorSlotChain extends AbstractProcessorSlotChain {

    AbstractProcessorSlot<?> first = new AbstractProcessorSlot<Object>() {

        @Override
        public void entry() throws Throwable {
            super.fireEntry();
        }

        @Override
        public void exit() {
            super.fireExit();
        }

    };
    AbstractProcessorSlot<?> end = first;

    @Override
    public void addFirst(AbstractProcessorSlot<?> protocolProcessor) {
        protocolProcessor.setNext(first.getNext());
        first.setNext(protocolProcessor);
        if (end == first) {
            end = protocolProcessor;
        }
    }

    @Override
    public void addLast(AbstractProcessorSlot<?> protocolProcessor) {
        end.setNext(protocolProcessor);
        end = protocolProcessor;
    }

    @Override
    public void setNext(AbstractProcessorSlot<?> next) {
        addLast(next);
    }

    @Override
    public AbstractProcessorSlot<?> getNext() {
        return first.getNext();
    }

    @Override
    public void entry() throws Throwable {
        first.transformEntry();
    }

    @Override
    public void exit() {
        first.exit();
    }

}
