package com.safecode.cyberzone.auth.controller;

import java.security.Principal;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.safecode.cyberzone.auth.auth.AuthHolder;
import com.safecode.cyberzone.auth.entity.SysLog;
import com.safecode.cyberzone.auth.entity.SysUser;
import com.safecode.cyberzone.auth.service.SysLogService;
import com.safecode.cyberzone.auth.service.SysUserService;
import com.safecode.cyberzone.auth.utils.IpUtil;
import com.safecode.cyberzone.base.dto.ResponseData;

@RestController
public class SsoController {
    private static final Authentication UsernamePasswordAuthenticationToken = null;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ConsumerTokenServices consumerTokenServices;

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysLogService sysLogService;

    @Autowired
    private HttpServletRequest request;

    // 撤销访问令牌
    @SuppressWarnings({"rawtypes", "unchecked"})
    @PostMapping("/tokens/revokeAccessToken")
    public ResponseData revokeAccessToken(@RequestParam("access_token") String access_token) {

        sysLogService.save(SysLog.builder().remark(AuthHolder.getCurrentUsername() + " :退出成功").userId(AuthHolder.getCurrentUserId().longValue())
                .projectName("cyberzone-auth").requestObject("").requestUrl("/tokens/revokeAccessToken")
                .createTime(new Date()).userName(AuthHolder.getCurrentUsername()).ip(IpUtil.getRemoteIp(request))
                .logStatus("登出日志").build());

        consumerTokenServices.revokeToken(access_token);
        return new ResponseData(HttpStatus.OK.value(), "撤销访问令牌成功", null);
    }

    // 撤销刷新令牌
    @SuppressWarnings({"rawtypes", "unchecked"})
    @PostMapping("/tokens/revokeRefreshToken")
    public ResponseData revokeRefreshToken(@RequestParam("refresh_token") String refresh_token) {
        if (tokenStore instanceof RedisTokenStore) {
            ((RedisTokenStore) tokenStore).removeRefreshToken(refresh_token);
        }
        return new ResponseData(HttpStatus.OK.value(), "撤销刷新令牌成功", null);
    }

    // 用户信息
    // 只返回principal
    @GetMapping("/user")
    public UserDetails userDetails(@AuthenticationPrincipal UserDetails userDetails) {
        return userDetails;
    }

    @GetMapping("/principal")
    public Principal principal(Principal principal) {
        return principal;
    }

    @GetMapping("/sysTime")
    public ResponseData<Long> sysTime() {
        return new ResponseData<Long>(HttpStatus.OK.value(), "查询成功", System.currentTimeMillis());
    }

    /**
     * 开启用户锁定状态
     *
     * @return
     */
    @PostMapping("/status")
    public ResponseData<SysUser> openAccountLocked(@RequestParam("id") Integer id,
                                                   @RequestParam("accountNonLocked") Integer accountNonLocked) {
        sysUserService.updateAccountLocked(id, accountNonLocked);
        SysUser user = sysUserService.selectByPrimaryKey(id);
        sysLogService
                .save(SysLog.builder()
                        .remark(AuthHolder.getCurrentUsername() + " :操作用户ID为:" + id + ",账号为：" + user.getAccount()
                                + "的锁定状态")
                        .projectName("cyberzone-auth").requestObject("").requestUrl("/open").createTime(new Date())
                        .userId(AuthHolder.getCurrentUserId().longValue()).userName(AuthHolder.getCurrentUsername()).ip(IpUtil.getRemoteIp(request)).logStatus("操作日志")
                        .build());
        return new ResponseData<SysUser>(HttpStatus.OK.value(), "操作成功", null);
    }

}
