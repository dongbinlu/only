package com.jiagouedu.services.front.email;import com.jiagouedu.core.Services;import com.jiagouedu.services.front.email.bean.Email;public interface EmailService extends Services<Email> {    /**     * 用户注册的时候，如果发出了多封邮件，则激活的时候，此方法使所有发出的链接都失效     *     * @param email     */    void updateEmailInvalidWhenReg(Email email);}