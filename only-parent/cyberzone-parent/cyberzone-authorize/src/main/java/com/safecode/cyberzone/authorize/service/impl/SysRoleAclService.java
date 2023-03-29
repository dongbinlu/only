package com.safecode.cyberzone.authorize.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.safecode.cyberzone.authorize.auth.AuthHolder;
import com.safecode.cyberzone.authorize.entity.SysAcl;
import com.safecode.cyberzone.authorize.entity.SysRole;
import com.safecode.cyberzone.authorize.entity.SysRoleAcl;
import com.safecode.cyberzone.authorize.exception.ParamException;
import com.safecode.cyberzone.authorize.mapper.SysAclMapper;
import com.safecode.cyberzone.authorize.mapper.SysRoleAclMapper;
import com.safecode.cyberzone.authorize.mapper.SysRoleMapper;

@Service("sysRoleAclService")
@Transactional
public class SysRoleAclService {
    @Autowired
    private SysRoleAclMapper sysRoleAclMapper;

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysAclMapper sysAclMapper;

    public void changRoleAcls(Integer roleId, List<String> aclCodesList) {
        // 获取roleId之前分配的权限码
        List<String> originAclCodeList = sysRoleAclMapper
                .getCodeIdListByRoleIdList(Lists.<Integer>newArrayList(roleId));

        if (originAclCodeList.size() == aclCodesList.size()) {
            Set<String> originAclCodeSet = Sets.newHashSet(originAclCodeList);
            Set<String> aclCodeSet = Sets.newHashSet(aclCodesList);
            originAclCodeSet.removeAll(aclCodeSet);
            if (CollectionUtils.isEmpty(originAclCodeSet)) {
                return;
            }
        }
        updateRoleAcls(roleId, aclCodesList);
    }

    public void updateRoleAcls(int roleId, List<String> aclCodesList) {
        if (CollectionUtils.isNotEmpty(aclCodesList)) {
            // 互拆判断
            // 角色授权，未勾选(靶场用户管理员，资源库管理员，测评管理员，内网安全态势管理员)任意一个时，勾选账号管理，角色管理，提示异常
            // 1，获取账号管理权限点 获取角色管理权限点
            List<SysAcl> accountRoleAcl = sysAclMapper.getByAclModuleIdList(Lists.<Integer>newArrayList(20, 21));
            List<String> accountRoleCodeIds = accountRoleAcl.stream().map(sysAcl -> sysAcl.getCodeId())
                    .collect(Collectors.toList());

            // 2，判断aclCodesList里面是否包含账号管理和角色管理权限点
            boolean flag = false;
            for (String accountRoleCodeId : accountRoleCodeIds) {
                if (aclCodesList.contains(accountRoleCodeId)) {
                    // 3，如果包含，再次判断aclCodesList里面是否包含靶场用户管理员，资源库管理员，测评管理员，内网安全态势管理员权限点
                    for (String adminCodeId : getAdminCodeId()) {
                        if (aclCodesList.contains(adminCodeId)) {
                            flag = true;
                            break;
                        }
                    }
                    if (!flag) {
                        throw new ParamException("授权不合法");
                    }
                }
            }

            // 3，判断aclCodesList里面是否包含管理员
            boolean boo = false;
            for (String adminCodeId : getAdminCodeId()) {
                // 3，如果包含，再次判断aclCodesList里面是否包含账号管理和角色管理权限点
                if (aclCodesList.contains(adminCodeId)) {
                    for (String accountRoleCodeId : accountRoleCodeIds) {
                        if (aclCodesList.contains(accountRoleCodeId)) {
                            boo = true;
                            break;
                        }
                    }
                    if (!boo) {
                        throw new ParamException("授权不合法");
                    }
                }
            }

        }

        sysRoleAclMapper.deleteByRoleId(roleId);
        // 更新角色关联的平台
        SysRole build = SysRole.builder().id(roleId).platformName(getPlatformByAclCodesList(aclCodesList))
                .admin(getAdmin(aclCodesList)).build();
        sysRoleMapper.updateByPrimaryKeySelective(build);

        if (CollectionUtils.isEmpty(aclCodesList)) {
            return;
        }
        List<SysRoleAcl> roleAclList = Lists.newArrayList();
        for (String aclCode : aclCodesList) {
            SysRoleAcl roleAcl = SysRoleAcl.builder().roleId(roleId).codeId(aclCode)
                    .operator(AuthHolder.getCurrentUsername()).operateIp(AuthHolder.getRemoteIp())
                    .operateTime(new Date()).build();
            roleAclList.add(roleAcl);
        }
        sysRoleAclMapper.batchInsert(roleAclList);

        SysRole builds = SysRole.builder().id(roleId).platformName(getPlatformByAclCodesList(aclCodesList))
                .admin(getAdmin(aclCodesList)).build();
        sysRoleMapper.updateByPrimaryKeySelective(builds);
    }

    // 根据权限点查平台
    // 运营管理中心：url:/operate code_id:20190821161435_23
    // 资源库：url:/resource code_id:20190821154928_49
    // 测评服务平台：url:/detection code_id:20190821154954_77
    // 内网安全可视化平台：url:/situationalAwareness code_id:20200513134426_7
    // 靶场支撑平台：url:/permission/check/brace code_id:20200513150608_16

    // 不勾选 平台id如何自己选择

    // 1，获取资源库下的所有权限点

    // 2，如果权限码在资源库权限点下，选中
    public String getPlatformByAclCodesList(List<String> aclCodesList) {

        Set<String> platformName = Sets.newHashSet();

        List<String> aclCodes = aclCodesList.stream().distinct().collect(Collectors.toList());
        for (String aclCode : aclCodes) {
            if (sysAclMapper.getByLevel("0.13").stream().map(sysAcl -> sysAcl.getCodeId()).collect(Collectors.toSet())
                    .contains(aclCode)) {
                platformName.add("运营管理中心");
            }
            if (sysAclMapper.getByLevel("0.14").stream().map(sysAcl -> sysAcl.getCodeId()).collect(Collectors.toSet())
                    .contains(aclCode)) {
                platformName.add("资源库");
            }
            if (sysAclMapper.getByLevel("0.17").stream().map(sysAcl -> sysAcl.getCodeId()).collect(Collectors.toSet())
                    .contains(aclCode)) {
                platformName.add("测评服务平台");
            }
            if (sysAclMapper.getByLevel("0.18").stream().map(sysAcl -> sysAcl.getCodeId()).collect(Collectors.toSet())
                    .contains(aclCode)) {
                platformName.add("内网安全可视化平台");
            }
        }

        return String.join(",", platformName);
    }

    // 平台管理员区分
    // 运营管理中心管理员 20200519094204_64
    // 资源库管理员 20200508153536_36
    // 测评管理员 20200508153611_72
    // 内网安全可视化平台管理员 20200508153634_42
    public String getAdmin(List<String> aclCodesList) {
        Set<String> admin = Sets.newHashSet();
        List<String> aclCodes = aclCodesList.stream().distinct().collect(Collectors.toList());
        for (String aclCode : aclCodes) {
            if (aclCode.equals("20200519094204_64")) {
                admin.add("靶场用户管理员");
            }
            if (aclCode.equals("20200508153536_36")) {
                admin.add("资源库管理员");
            }
            if (aclCode.equals("20200508153611_72")) {
                admin.add("测评服务平台管理员");
            }
            if (aclCode.equals("20200508153634_42")) {
                admin.add("内网安全可视化平台管理员");
            }
        }

        return String.join(",", admin);
    }

    public List<String> getAdminCodeId() {
        ArrayList<String> list = Lists.newArrayList();
        list.add("20200519094204_64");
        list.add("20200508153536_36");
        list.add("20200508153611_72");
        list.add("20200508153634_42");
        return list;
    }
}
