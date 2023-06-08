package com.only.test.Design.chain.plus;

public interface IProcessorSlot<T> {


    void entry() throws Throwable;

    void fireEntry() throws Throwable;

    void exit();

    void fireExit();

}
