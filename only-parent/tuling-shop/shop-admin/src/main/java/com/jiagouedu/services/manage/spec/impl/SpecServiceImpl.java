package com.jiagouedu.services.manage.spec.impl;import com.jiagouedu.core.ServersManager;import com.jiagouedu.services.manage.spec.SpecService;import com.jiagouedu.services.manage.spec.bean.Spec;import com.jiagouedu.services.manage.spec.dao.SpecDao;import org.springframework.stereotype.Service;import javax.annotation.Resource;@Service("specServiceManage")public class SpecServiceImpl extends ServersManager<Spec, SpecDao> implements        SpecService {    @Resource(name = "specDaoManage")    @Override    public void setDao(SpecDao specDao) {        this.dao = specDao;    }}