package com.safecode.cyberzone.auth.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Preconditions;
import com.safecode.cyberzone.auth.entity.SysUser;
import com.safecode.cyberzone.auth.mapper.SysUserMapper;
import com.safecode.cyberzone.auth.service.SysUserService;

@Service("sysUserService")
@Transactional
public class SysUserServiceImpl implements SysUserService {
    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private LoginAttemptService loginAttemptService;

    @Override
    public SysUser findByKeyword(String keyword) {
        SysUser sysUser = sysUserMapper.findByKeyword(keyword);
        if (sysUser != null) {
            return sysUser;
        }
        return null;
    }

    @Override
    public SysUser selectByPrimaryKey(Integer userId) {
        SysUser sysUser = sysUserMapper.selectByPrimaryKey(userId);
        Preconditions.checkNotNull(sysUser, "该用户不存在");
        return sysUser;
    }

    @Override
    public void updateAccountLocked(Integer id, Integer accountNonLocked) {
        SysUser sysUser = sysUserMapper.selectByPrimaryKey(id);
        Preconditions.checkNotNull(sysUser, "用户不存在");
        //开启
        if (accountNonLocked.intValue() == 1) {
            sysUserMapper.updateByPrimaryKeySelective(SysUser.builder().id(id).accountNonLocked(accountNonLocked).build());
            loginAttemptService.loginSucceeded(sysUser.getAccount());
            loginAttemptService.loginSucceeded(sysUser.getTelephone());
            loginAttemptService.loginSucceeded(sysUser.getMail());
        }
        if (accountNonLocked.intValue() == 0) {
            sysUserMapper.updateByPrimaryKeySelective(SysUser.builder().id(id).accountNonLocked(accountNonLocked).build());
        }
    }

}
