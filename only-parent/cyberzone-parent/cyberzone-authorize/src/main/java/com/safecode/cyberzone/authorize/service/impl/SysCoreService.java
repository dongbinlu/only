package com.safecode.cyberzone.authorize.service.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.safecode.cyberzone.authorize.entity.SysAcl;
import com.safecode.cyberzone.authorize.entity.SysUser;
import com.safecode.cyberzone.authorize.mapper.SysAclMapper;
import com.safecode.cyberzone.authorize.mapper.SysRoleAclMapper;
import com.safecode.cyberzone.authorize.mapper.SysRoleUserMapper;
import com.safecode.cyberzone.authorize.mapper.SysUserMapper;
import com.safecode.cyberzone.authorize.properties.SecurityProperties;

@Service("sysCoreService")
public class SysCoreService {

    @Autowired
    private SysAclMapper sysAclMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysRoleUserMapper sysRoleUserMapper;

    @Autowired
    private SysRoleAclMapper sysRoleAclMapper;

    @Autowired
    private SecurityProperties securityProperties;

    // 当前角色分配的权限点
    public List<SysAcl> getRoleAclList(int roleId) {
        // 根据角色id获取权限码
        List<String> aclCodeList = sysRoleAclMapper.getCodeIdListByRoleIdList(Lists.<Integer>newArrayList(roleId));
        if (CollectionUtils.isEmpty(aclCodeList)) {
            return Lists.newArrayList();
        }
        // 根据权限码获取权限点
        List<Integer> aclIdList = sysAclMapper.getAclIdListByCodeList(aclCodeList);
        if (CollectionUtils.isEmpty(aclIdList)) {
            return Lists.newArrayList();
        }
        // 根据权限点获取权限
        return sysAclMapper.getByIdList(aclIdList);
    }

    // 当前用户已分配的权限点
    public List<SysAcl> getCurrentUserAclList(Integer userId) {
        return getUserAclList(userId);
    }

    public List<SysAcl> getUserAclList(int userId) {
        if (isSuperAdmin(userId)) {
            return sysAclMapper.getAll();
        }
        // 根据用户Id获取角色ID
        List<Integer> userRoleIdList = sysRoleUserMapper.getRoleIdListByUserId(userId);
        if (CollectionUtils.isEmpty(userRoleIdList)) {
            return Lists.newArrayList();
        }
        // 根据角色ID获取权限点ID

        // 根据角色id获取权限码
        List<String> aclCodeList = sysRoleAclMapper.getCodeIdListByRoleIdList(userRoleIdList);
        if (CollectionUtils.isEmpty(aclCodeList)) {
            return Lists.newArrayList();
        }
        // 根据权限码获取权限点
        List<Integer> userAclIdList = sysAclMapper.getAclIdListByCodeList(aclCodeList);

        if (CollectionUtils.isEmpty(userAclIdList)) {
            return Lists.newArrayList();
        }
        // 根据权限点获取权限点List
        return sysAclMapper.getByIdList(userAclIdList);

    }

    public boolean isSuperAdmin(int userId) {
        // 这里是我自己定义了一个假的超级管理员规则，实际中要根据项目进行修改
        // 可以是配置文件获取，可以指定某个用户，也可以指定某个角色
        SysUser sysUser = sysUserMapper.selectByPrimaryKey(userId);
        if (securityProperties.getOther().getSuperAdministrator().equals(sysUser.getMail())) {
            return true;
        }
        return false;
    }
}
