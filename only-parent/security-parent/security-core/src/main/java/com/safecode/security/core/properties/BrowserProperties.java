package com.safecode.security.core.properties;

public class BrowserProperties {

    private SessionProperties session = new SessionProperties();

    // 系统默认登录页
    private String loginPage = "/signIn.html";

    // 社交登录和业务系统的注册页
    private String signUpUrl = "/signUp.html";

    private String signOutUrl = "JSON";

    // 登录成功默认返回json
    private LoginType loginType = LoginType.JSON;

    // 记住我 默认1周
    private int rememberMeSeconds = 604800;

    public String getLoginPage() {
        return loginPage;
    }

    public void setLoginPage(String loginPage) {
        this.loginPage = loginPage;
    }

    public LoginType getLoginType() {
        return loginType;
    }

    public void setLoginType(LoginType loginType) {
        this.loginType = loginType;
    }

    public int getRememberMeSeconds() {
        return rememberMeSeconds;
    }

    public void setRememberMeSeconds(int rememberMeSeconds) {
        this.rememberMeSeconds = rememberMeSeconds;
    }

    public String getSignUpUrl() {
        return signUpUrl;
    }

    public void setSignUpUrl(String signUpUrl) {
        this.signUpUrl = signUpUrl;
    }

    public String getSignOutUrl() {
        return signOutUrl;
    }

    public void setSignOutUrl(String signOutUrl) {
        this.signOutUrl = signOutUrl;
    }

    public SessionProperties getSession() {
        return session;
    }

    public void setSession(SessionProperties session) {
        this.session = session;
    }

}
