package com.jiagouedu.services.front.keyvalue.impl;import com.jiagouedu.core.ServersManager;import com.jiagouedu.services.front.keyvalue.KeyvalueService;import com.jiagouedu.services.front.keyvalue.bean.Keyvalue;import com.jiagouedu.services.front.keyvalue.dao.KeyvalueDao;import org.springframework.beans.factory.annotation.Autowired;import org.springframework.stereotype.Service;import javax.annotation.Resource;@Servicepublic class KeyvalueServiceImpl extends ServersManager<Keyvalue, KeyvalueDao> implements        KeyvalueService {    @Resource    @Override    public void setDao(KeyvalueDao keyvalueDao) {        this.dao = keyvalueDao;    }}