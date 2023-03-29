package com.safecode.cyberzone.auth.properties;

/**
 * 认证服务器注册的第三方应用配置项
 *
 * @author v_boy
 */
public class OAuth2ClientProperties {

    // 第三方应用appId
    private String clientId;

    // 第三方应用appSecret
    private String clientSecret;

    // 针对此应用发出的token的有效时间
    private int accessTokenValidateSeconds = 7200;

    // 刷新token的有效时间
    private int refreshTokenValiditySeconds = 7200;

    private String authorizedGrantTypes;

    private String scopes = "all";

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public int getAccessTokenValidateSeconds() {
        return accessTokenValidateSeconds;
    }

    public void setAccessTokenValidateSeconds(int accessTokenValidateSeconds) {
        this.accessTokenValidateSeconds = accessTokenValidateSeconds;
    }

    public int getRefreshTokenValiditySeconds() {
        return refreshTokenValiditySeconds;
    }

    public void setRefreshTokenValiditySeconds(int refreshTokenValiditySeconds) {
        this.refreshTokenValiditySeconds = refreshTokenValiditySeconds;
    }

    public String getAuthorizedGrantTypes() {
        return authorizedGrantTypes;
    }

    public void setAuthorizedGrantTypes(String authorizedGrantTypes) {
        this.authorizedGrantTypes = authorizedGrantTypes;
    }

    public String getScopes() {
        return scopes;
    }

    public void setScopes(String scopes) {
        this.scopes = scopes;
    }

}
