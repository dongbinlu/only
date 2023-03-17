package com.only.test.template.method.processor;

public interface Processor {

    /**
     * 应用标识
     *
     * @param flag
     * @return
     */
    boolean support(byte flag);

}
