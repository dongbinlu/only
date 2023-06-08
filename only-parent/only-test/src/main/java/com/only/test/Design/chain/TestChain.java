package com.only.test.Design.chain;

import org.junit.Before;
import org.junit.Test;

public class TestChain {

    public Boss boss;

    public Sales sales;

    public Product product;

    @Before
    public void before() {

        boss = new Boss("老板");
        sales = new Sales("销售");
        product = new Product("产品经理");

        product.setHandler(sales);
        sales.setHandler(boss);

    }

    @Test
    public void test(){
        product.handle(2220);
    }


}
