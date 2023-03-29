package com.safecode.cyberzone.authorize.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.github.pagehelper.PageInfo;
import com.safecode.cyberzone.authorize.dto.RoleDto;
import com.safecode.cyberzone.authorize.entity.SysRole;
import com.safecode.cyberzone.authorize.entity.SysUser;
import com.safecode.cyberzone.authorize.param.RoleParam;

public interface SysRoleService {

    void save(RoleParam param);

    void update(RoleParam param);

    List<SysRole> getAll();

    PageInfo<RoleDto> newGetAll(Pageable pageable, RoleDto dto);

    List<SysRole> getRoleListByUserId(int userId);

    List<SysRole> getRoleListByAclId(int aclId);

    List<SysUser> getUserListByRoleList(List<SysRole> roleList);

    public void delete(int roleId);

    public void batchDelete(List<Integer> roleIdList);

    PageInfo<SysUser> roleToUser(Pageable pageable, Integer roleId, String account, String username);

    public SysRole id(Integer id);

    public void remove(Integer roleId, Integer userId);

    public void batchRemove(Integer roleId, String userIds);

    public void writeExecuteRoleIdToRedis();
}
