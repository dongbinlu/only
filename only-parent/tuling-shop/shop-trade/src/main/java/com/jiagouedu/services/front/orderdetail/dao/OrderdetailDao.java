package com.jiagouedu.services.front.orderdetail.dao;import com.jiagouedu.core.DaoManager;import com.jiagouedu.services.front.orderdetail.bean.Orderdetail;public interface OrderdetailDao extends DaoManager<Orderdetail> {    int selectCount(String orderID);}