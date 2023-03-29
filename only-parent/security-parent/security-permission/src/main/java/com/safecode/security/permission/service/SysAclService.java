package com.safecode.security.permission.service;

import org.springframework.data.domain.Pageable;

import com.github.pagehelper.PageInfo;
import com.safecode.security.permission.entity.SysAcl;
import com.safecode.security.permission.param.AclParam;

public interface SysAclService {

    public void save(AclParam param);

    public void update(AclParam param);

    public PageInfo<SysAcl> getPageByAclModuleId(Integer aclModuleId, Pageable pageable);

    public void delete(Integer id);

}
