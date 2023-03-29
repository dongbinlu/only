package com.safecode.cyberzone.auth.service;

import com.safecode.cyberzone.auth.entity.SysUser;

public interface SysUserService {
    public SysUser findByKeyword(String keyword);

    public SysUser selectByPrimaryKey(Integer id);

    public void updateAccountLocked(Integer id, Integer accountNonLocked);

}
