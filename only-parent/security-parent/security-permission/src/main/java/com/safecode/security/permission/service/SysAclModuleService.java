package com.safecode.security.permission.service;

import com.safecode.security.permission.param.AclModuleParam;

public interface SysAclModuleService {

    public void save(AclModuleParam param);

    public void update(AclModuleParam param);

    public void delete(Integer id);

}
