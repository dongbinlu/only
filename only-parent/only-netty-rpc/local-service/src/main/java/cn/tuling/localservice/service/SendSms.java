package cn.tuling.localservice.service;


import cn.tuling.localservice.vo.UserInfo;

/**
 *
 *类说明：短信息发送接口
 */
public interface SendSms {
    /*发送短信*/
    boolean sendMail(UserInfo user);

}
