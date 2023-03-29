package com.safecode.cyberzone.authorize.service;

import org.springframework.data.domain.Pageable;

import com.github.pagehelper.PageInfo;
import com.safecode.cyberzone.authorize.entity.SysAcl;
import com.safecode.cyberzone.authorize.param.AclParam;

public interface SysAclService {
    void save(AclParam param);

    void update(AclParam param);

    PageInfo<SysAcl> getPageByAclModuleId(Integer aclModuleId, Pageable pageable);

    SysAcl selectByPrimaryKey(Integer id);

    public void delete(Integer aclId);
}
