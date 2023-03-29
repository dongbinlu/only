package com.safecode.security.core.social.qq.api;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.oauth2.AbstractOAuth2ApiBinding;
import org.springframework.social.oauth2.TokenStrategy;

import com.fasterxml.jackson.databind.ObjectMapper;


//获取用户信息
public class QQImpl extends AbstractOAuth2ApiBinding implements QQ {

    //access_token 父类已经有了
    //oauth_consumer_key 申请qq登录成功后，分配给应用的appid
    //openid 用户的ID，与QQ号码一一对应。 可通过调用https://graph.qq.com/oauth2.0/me?access_token=YOUR_ACCESS_TOKEN 来获取。

    private Logger logger = LoggerFactory.getLogger(getClass());

    private static final String URL_GET_OPENID = "https://graph.qq.com/oauth2.0/me?access_token=%s";

    private static final String URL_GET_USERINFO = "https://graph.qq.com/user/get_user_info?oauth_consumer_key=%s&openid=%s";

    private String appId;
    private String openId;//通过access_token获取

    private ObjectMapper objectMapper = new ObjectMapper();

    public QQImpl(String accessToken, String appId) {
        super(accessToken, TokenStrategy.ACCESS_TOKEN_PARAMETER);
        this.appId = appId;

        String url = String.format(URL_GET_OPENID, accessToken);

        //获取openid  {"client_id":"YOUR_APPID","openid":"YOUR_OPENID"};
        String result = getRestTemplate().getForObject(url, String.class);

        logger.info("获取openId" + result);

        this.openId = StringUtils.substringBetween(result, "\"openid\":\"", "\"}");

    }

    @Override
    public QQUserInfo getUserInfo() {
        String url = String.format(URL_GET_USERINFO, appId, openId);

        //用户信息
        String result = getRestTemplate().getForObject(url, String.class);

        logger.info("用户信息" + result);

        QQUserInfo userInfo = null;

        // 将json格式的字符串转java对象
        try {
            userInfo = objectMapper.readValue(result, QQUserInfo.class);

            //将openid设置进去
            userInfo.setOpenId(openId);
            return userInfo;
        } catch (Exception e) {
            throw new RuntimeException("获取用户信息失败", e);
        }
    }

}
