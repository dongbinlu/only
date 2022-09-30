package com.only.test.ioc;

public class InstanceB {

    private InstanceA instanceA;

/*    public InstanceB(InstanceA instanceA) {
        this.instanceA = instanceA;
    }*/

    public InstanceA getInstanceA() {
        return instanceA;
    }

    public void setInstanceA(InstanceA instanceA) {
        this.instanceA = instanceA;
    }
}
