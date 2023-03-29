package com.safecode.cyberzone.auth.service.impl;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.safecode.cyberzone.auth.entity.AuthUser;
import com.safecode.cyberzone.auth.entity.StatusDto;
import com.safecode.cyberzone.auth.entity.SysAcl;
import com.safecode.cyberzone.auth.entity.SysUser;
import com.safecode.cyberzone.auth.exception.SsoOAuth2Exception;
import com.safecode.cyberzone.auth.mapper.SysUserMapper;
import com.safecode.cyberzone.auth.properties.SecurityProperties;
import com.safecode.cyberzone.auth.service.SysUserService;

@Service("ssoUserDetailsService")
public class SsoUserDetailsService implements UserDetailsService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SysCoreService sysCoreService;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private LoginAttemptService loginAttemptService;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        logger.info("登录用户名：" + username);
        String ip = request.getRemoteAddr();
        SysUser sysUser = sysUserService.findByKeyword(username);
        if (loginAttemptService.isBlocked(username)) {
            sysUser.setAccountNonLocked(0);
            sysUserMapper.updateByPrimaryKeySelective(sysUser);
            throw new SsoOAuth2Exception("账号已被禁用,请联系管理员");
        }
        if (null != sysUser) {
            StatusDto status = getStatusDto(sysUser);
            return new AuthUser(sysUser.getId(), username, sysUser.getUsername(), sysUser.getPassword(),
                    sysUser.getFacePerm(), sysUser.getFaceId(), status.isStatus(), status.isAccountNonExpired(),
                    status.isCredentialsNonExpired(), status.isAccountNonLocked(),
                    AuthorityUtils.commaSeparatedStringToAuthorityList(getAcls(sysUser.getId())));
        } else {
            throw new SsoOAuth2Exception("用户名或密码错误");
        }
    }

    public String getAcls(Integer id) {
        String otherUrl = securityProperties.getPermit().getOtherUrl();

        StringBuilder builder = new StringBuilder();
        List<SysAcl> acls = sysCoreService.getCurrentUserAclList(id);
        for (SysAcl acl : acls) {
            if (acl.getUrl() != null && acl.getStatus() == 1) {
                builder.append(acl.getUrl()).append(",");
            }
        }
        builder.append(otherUrl);
        return builder.toString();
    }

    public StatusDto getStatusDto(SysUser sysUser) {
        if (sysUser.getCredentialsNonExpired() == 0 || new Date().after(sysUser.getCredentialsExpired())) {
            // 密码过期
            sysUser.setCredentialsNonExpired(0);
            sysUserMapper.updateByPrimaryKeySelective(sysUser);
        }
        StatusDto statusDto = StatusDto.builder().status(sysUser.getStatus() == 1 ? true : false)
                .accountNonExpired(sysUser.getAccountNonExpired() == 1 ? true : false)
                .credentialsNonExpired(sysUser.getCredentialsNonExpired() == 1 ? true : false)
                .accountNonLocked(sysUser.getAccountNonLocked() == 1 ? true : false).build();
        return statusDto;
    }

}
