package com.jiagouedu.services.front.email.dao;import com.jiagouedu.core.DaoManager;import com.jiagouedu.services.front.email.bean.Email;public interface EmailDao extends DaoManager<Email> {    void updateEmailInvalidWhenReg(Email email);}