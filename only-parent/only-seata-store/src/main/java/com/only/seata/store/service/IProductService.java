package com.only.seata.store.service;



public interface IProductService {

    boolean reduceCount(Integer amount, Integer productId) throws Exception;
}
