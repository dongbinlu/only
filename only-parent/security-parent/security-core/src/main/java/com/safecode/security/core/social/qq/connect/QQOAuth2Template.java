package com.safecode.security.core.social.qq.connect;

import java.nio.charset.Charset;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2Template;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class QQOAuth2Template extends OAuth2Template {

    private Logger logger = LoggerFactory.getLogger(getClass());

    public QQOAuth2Template(String clientId, String clientSecret, String authorizeUrl, String accessTokenUrl) {
        super(clientId, clientSecret, authorizeUrl, accessTokenUrl);
        //只有设置为true时，源码跟踪才会带上clientId和clientSecret
        setUseParametersForClientAuthentication(true);
    }


    /**
     * 在走完第三步后放回授权码code之后，拿授权码换token时，出现解析异常，原因是restemplate不支持响应回来的html格式
     * 在OAuth2Template中用restemplate期望返回来的是json数据，实际上走第三步时返回的是html
     * 如果成功返回，即可在返回包中获取到Access Token。 如：access_token=FE04*****CCE2&expires_in=7776000&refresh_token=88E4**BE14
     * 需重写postForAccessGrant方法  将请求的结果按照qq的标准自定义解析
     */
    @Override
    protected AccessGrant postForAccessGrant(String accessTokenUrl, MultiValueMap<String, String> parameters) {
        String responseStr = getRestTemplate().postForObject(accessTokenUrl, parameters, String.class);

        logger.info("获取accessToken的响应：" + responseStr);

        String[] items = StringUtils.splitByWholeSeparatorPreserveAllTokens(responseStr, "&");

        String accessToken = StringUtils.substringAfterLast(items[0], "=");
        Long expiresIn = new Long(StringUtils.substringAfterLast(items[1], "="));
        String refreshToken = StringUtils.substringAfterLast(items[2], "=");

        return new AccessGrant(accessToken, null, refreshToken, expiresIn);
    }

    //添加text/html格式支持
    @Override
    protected RestTemplate createRestTemplate() {
        //先获取父类的结果
        RestTemplate restTemplate = super.createRestTemplate();
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter(Charset.forName("UTF-8")));

        return restTemplate;
    }

}
