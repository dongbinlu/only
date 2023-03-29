package com.safecode.cyberzone.authorize.service;

import com.safecode.cyberzone.authorize.param.AclModuleParam;

public interface SysAclModuleService {

    public void save(AclModuleParam param);

    public void update(AclModuleParam param);

    public void delete(Integer aclModuleId);

}
