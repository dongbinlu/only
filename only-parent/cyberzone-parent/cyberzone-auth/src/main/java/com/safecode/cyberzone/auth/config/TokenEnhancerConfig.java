package com.safecode.cyberzone.auth.config;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import com.safecode.cyberzone.auth.entity.AuthUser;
import com.safecode.cyberzone.auth.entity.SysLog;
import com.safecode.cyberzone.auth.service.SysLogService;
import com.safecode.cyberzone.auth.utils.IpUtil;

@Component("tokenEnhancerConfig")
public class TokenEnhancerConfig implements TokenEnhancer {

    @Autowired
    private SysLogService sysLogService;

    @Autowired
    private HttpServletRequest request;

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        final Map<String, Object> additionalInfo = new HashMap<>();
        AuthUser authUser = (AuthUser) authentication.getUserAuthentication().getPrincipal();
        additionalInfo.put("user_id", authUser.getUserId());
        additionalInfo.put("username", authUser.getUsername());
        additionalInfo.put("nickName", authUser.getNickName());
        if (0 == authUser.getFacePerm()) {
            additionalInfo.put("redirect", null);
        }
        if (1 == authUser.getFacePerm()) {
            if (authUser.getFaceId() != null && authUser.getFaceId().length() != 0) {
                additionalInfo.put("redirect", "scan");
            } else {
                additionalInfo.put("redirect", "reg");
            }
        }
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);

        sysLogService.save(SysLog.builder().remark(authUser.getUsername() + "登录成功").userId(authUser.getUserId().longValue())
                .projectName("cyberzone-auth").requestObject("").requestUrl("/oauth/token").createTime(new Date())
                .userName(authUser.getUsername()).ip(IpUtil.getRemoteIp(request)).logStatus("登录日志").build());

        return accessToken;
    }

}
