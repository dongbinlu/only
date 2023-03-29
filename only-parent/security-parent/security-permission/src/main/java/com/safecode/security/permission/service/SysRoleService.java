package com.safecode.security.permission.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.github.pagehelper.PageInfo;
import com.safecode.security.permission.entity.SysRole;
import com.safecode.security.permission.entity.SysUser;
import com.safecode.security.permission.param.RoleParam;

public interface SysRoleService {

    public void save(RoleParam param);

    public void update(RoleParam param);

    public PageInfo<SysRole> getPage(Pageable pageable);

    public List<SysRole> getRoleListByUserId(Integer userId);

    public List<SysRole> getRoleListByAclId(Integer aclId);

    public List<SysUser> getUserListByRoleList(List<SysRole> roleList);

    public void delete(Integer roleId);

}
