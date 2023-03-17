package com.only.test.template.method.processor;

public class Processor0050 implements Processor {

    @Override
    public boolean support(byte flag) {
        return flag == 0x00;
    }
}
