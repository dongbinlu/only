package com.only.test.proxy.jna;

public class Test {

    public static void main(String[] args) {
        ITunnel tunnel = Tunnel.getBean();

        tunnel.send();
    }
}
