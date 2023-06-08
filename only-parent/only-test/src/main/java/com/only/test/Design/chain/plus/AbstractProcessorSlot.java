package com.only.test.Design.chain.plus;

public abstract class AbstractProcessorSlot<T> implements IProcessorSlot<T> {


    private AbstractProcessorSlot<?> next = null;

    @Override
    public void fireEntry() throws Throwable {
        if (next != null) {
            next.transformEntry();
        }
    }

    void transformEntry() throws Throwable {
        entry();
    }

    @Override
    public void fireExit() {
        if (next != null) {
            next.exit();
        }
    }

    public AbstractProcessorSlot<?> getNext() {
        return next;
    }

    public void setNext(AbstractProcessorSlot<?> next) {
        this.next = next;
    }
}
