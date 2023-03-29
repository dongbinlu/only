package com.safecode.cyberzone.auth.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.safecode.cyberzone.auth.entity.SysAcl;
import com.safecode.cyberzone.auth.entity.SysUser;
import com.safecode.cyberzone.auth.mapper.SysAclMapper;
import com.safecode.cyberzone.auth.mapper.SysRoleAclMapper;
import com.safecode.cyberzone.auth.mapper.SysRoleUserMapper;
import com.safecode.cyberzone.auth.mapper.SysUserMapper;
import com.safecode.cyberzone.auth.properties.SecurityProperties;

@Service("sysCoreService")
public class SysCoreService {

    @Autowired
    private SysAclMapper sysAclMapper;

    @Autowired
    private SysRoleUserMapper sysRoleUserMapper;

    @Autowired
    private SysRoleAclMapper sysRoleAclMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SecurityProperties securityProperties;

    // 当前用户已分配的权限点
    public List<SysAcl> getCurrentUserAclList(Integer currentUserId) {
        return getUserAclList(currentUserId);
    }

    public List<SysAcl> getUserAclList(int userId) {
        if (isSuperAdmin(userId)) {
            SysAcl sysAcl = new SysAcl();
            sysAcl.setStatus(1);
            sysAcl.setUrl("/**");
            // return sysAclMapper.getAll();
            List<SysAcl> list = new ArrayList<>();
            list.add(sysAcl);
            return list;
        }
        // 根据用户Id获取角色ID
        List<Integer> userRoleIdList = sysRoleUserMapper.getRoleIdListByUserId(userId);
        if (CollectionUtils.isEmpty(userRoleIdList)) {
            return Lists.newArrayList();
        }
        /*
         * 根据角色ID获取权限点ID
         */

        // 根据角色id获取权限码
        List<String> codeIdList = sysRoleAclMapper.getCodeIdListByRoleIdList(userRoleIdList);

        if (CollectionUtils.isEmpty(codeIdList)) {
            return Lists.newArrayList();
        }

        // 根据权限码获取权限点
        List<Integer> userAclIdList = sysAclMapper.getAclIdListByCodeList(codeIdList);

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
