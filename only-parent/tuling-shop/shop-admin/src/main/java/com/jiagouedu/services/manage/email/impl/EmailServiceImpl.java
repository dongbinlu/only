package com.jiagouedu.services.manage.email.impl;import com.jiagouedu.core.ServersManager;import com.jiagouedu.services.manage.email.EmailService;import com.jiagouedu.services.manage.email.bean.Email;import com.jiagouedu.services.manage.email.dao.EmailDao;import org.springframework.stereotype.Service;import javax.annotation.Resource;@Service("emailServiceManage")public class EmailServiceImpl extends ServersManager<Email, EmailDao> implements        EmailService {    @Resource(name = "emailDaoManage")    @Override    public void setDao(EmailDao emailDao) {        this.dao = emailDao;    }}