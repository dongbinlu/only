package com.safecode.security.permission.service.impl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.safecode.security.permission.common.CacheKeyConstants;
import com.safecode.security.permission.common.RequestHolder;
import com.safecode.security.permission.entity.SysAcl;
import com.safecode.security.permission.entity.SysUser;
import com.safecode.security.permission.mapper.SysAclMapper;
import com.safecode.security.permission.mapper.SysRoleAclMapper;
import com.safecode.security.permission.mapper.SysRoleUserMapper;
import com.safecode.security.permission.utils.JsonMapper;

@Service("sysCoreService")
public class SysCoreService {

    @Autowired
    private SysAclMapper sysAclMapper;

    @Autowired
    private SysRoleUserMapper sysRoleUserMapper;

    @Autowired
    private SysRoleAclMapper sysRoleAclMapper;

    @Autowired
    private SysCacheService sysCacheService;

    // 获取当前登录用户的权限点list
    public List<SysAcl> getCurrentUserAclList() {
        Integer currentUserId = RequestHolder.getCurrentUser().getId();
        return getUserAclList(currentUserId);
    }

    public List<SysAcl> getUserAclList(int userId) {

        // 判断是否是超级管理员
        if (isSuperAdmin()) {
            return sysAclMapper.getAll();
        }

        // 根据用户id获取角色idList

        List<Integer> userRoleIdList = sysRoleUserMapper.getRoleIdListByUserId(userId);
        if (CollectionUtils.isEmpty(userRoleIdList)) {
            return Lists.newArrayList();
        }

        // 通过角色idList获取权限码idList
        List<String> roleCodeIdList = sysRoleAclMapper.getCodeIdListByRoleIdList(userRoleIdList);
        if (CollectionUtils.isEmpty(roleCodeIdList)) {
            return Lists.newArrayList();
        }

        // 通过权限码id获取权限
        return sysAclMapper.getByRoleCodeIdList(roleCodeIdList);

    }

    public List<SysAcl> getRoleAclList(int roleId) {
        List<String> roleCodeIdList = sysRoleAclMapper.getCodeIdListByRoleIdList(Lists.<Integer>newArrayList(roleId));
        if (CollectionUtils.isEmpty(roleCodeIdList)) {
            return Lists.newArrayList();
        }
        return sysAclMapper.getByRoleCodeIdList(roleCodeIdList);

    }

    private boolean isSuperAdmin() {
        SysUser sysUser = RequestHolder.getCurrentUser();
        if (sysUser.getMail().contains("admin")) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否有权限访问
     *
     * @param url
     * @return
     */
    public boolean hasUrlAcl(String url) {

        if (isSuperAdmin()) {
            return true;
        }

        // 从数据库里面取url,如果没有返回true,防止404和无权限访问冲突
        List<SysAcl> aclList = sysAclMapper.getByUrl(url);
        if (CollectionUtils.isEmpty(aclList)) {
            return true;
        }

        // 当前用户的权限点
        List<SysAcl> userAclList = getCurrentUserAclList();
        // 从缓存中取 ,但数据同步有问题
        // List<SysAcl> userAclList = getCurrentUserAclListFromCatch();
        Set<Integer> userAclIdSet = userAclList.stream().map(acl -> acl.getId()).collect(Collectors.toSet());

        boolean hasValidAcl = false;

        // 规则：只要有一个权限点有权限，那么我们就认为有访问权限
        for (SysAcl acl : aclList) {

            // 判断一个用户是否具有某个权限点的访问权限
            if (acl == null || acl.getStatus() != 1) {// 权限点无效
                continue;
            }

            hasValidAcl = true;

            if (userAclIdSet.contains(acl.getId())) {
                return true;
            }
        }
        if (!hasValidAcl) {
            return true;
        }

        return false;

    }

    public List<SysAcl> getCurrentUserAclListFromCatch() {
        Integer userId = RequestHolder.getCurrentUser().getId();
        String cacheValue = sysCacheService.getCache(CacheKeyConstants.USER_ACLS, String.valueOf(userId));
        if (StringUtils.isBlank(cacheValue)) {
            List<SysAcl> aclList = getCurrentUserAclList();
            if (CollectionUtils.isNotEmpty(aclList)) {
                sysCacheService.saveCache(JsonMapper.obj2String(aclList), 600, CacheKeyConstants.USER_ACLS,
                        String.valueOf(userId));
            }
            return aclList;
        }
        return JsonMapper.string2Obj(cacheValue, new TypeReference<List<SysAcl>>() {
        });
    }

}
