package com.jiagouedu.services.front.systemlog.impl;import com.jiagouedu.core.ServersManager;import com.jiagouedu.services.front.systemlog.SystemlogService;import com.jiagouedu.services.front.systemlog.bean.Systemlog;import com.jiagouedu.services.front.systemlog.dao.SystemlogDao;import org.springframework.beans.factory.annotation.Autowired;import org.springframework.stereotype.Service;import javax.annotation.Resource;@Servicepublic class SystemlogServiceImpl extends ServersManager<Systemlog, SystemlogDao> implements        SystemlogService {    @Resource(name = "systemlogDao")    @Override    public void setDao(SystemlogDao systemlogDao) {        this.dao = systemlogDao;    }}