package com.only.test.ioc;

public class InstanceA {

    private InstanceB instanceB;

/*    public InstanceA(InstanceB instanceB) {
        this.instanceB = instanceB;
    }*/

    public InstanceB getInstanceB() {
        return instanceB;
    }

    public void setInstanceB(InstanceB instanceB) {
        this.instanceB = instanceB;
    }
}
