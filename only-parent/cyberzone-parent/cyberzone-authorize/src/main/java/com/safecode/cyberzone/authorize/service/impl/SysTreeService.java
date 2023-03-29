package com.safecode.cyberzone.authorize.service.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.safecode.cyberzone.authorize.dto.AclDto;
import com.safecode.cyberzone.authorize.dto.AclModuleLevelDto;
import com.safecode.cyberzone.authorize.dto.DeptLevelDto;
import com.safecode.cyberzone.authorize.entity.SysAcl;
import com.safecode.cyberzone.authorize.entity.SysAclModule;
import com.safecode.cyberzone.authorize.entity.SysDept;
import com.safecode.cyberzone.authorize.mapper.SysAclMapper;
import com.safecode.cyberzone.authorize.mapper.SysAclModuleMapper;
import com.safecode.cyberzone.authorize.mapper.SysDeptMapper;
import com.safecode.cyberzone.authorize.mapper.SysRoleAclMapper;
import com.safecode.cyberzone.authorize.mapper.SysRoleUserMapper;
import com.safecode.cyberzone.authorize.utils.LevelUtil;

@Service("sysTreeService")
public class SysTreeService {
    @Autowired
    private SysDeptMapper sysDeptMapper;

    @Autowired
    private SysAclModuleMapper sysAclModuleMapper;

    @Autowired
    private SysCoreService sysCoreService;

    @Autowired
    private SysAclMapper sysAclMapper;

    @Autowired
    private SysRoleUserMapper sysRoleUserMapper;

    @Autowired
    private SysRoleAclMapper sysRoleAclMapper;

    // 获取用户权限树
    public List<AclModuleLevelDto> userAclTree(int userId) {
        List<SysAcl> userAclList = sysCoreService.getUserAclList(userId);
        List<AclDto> aclDtoList = Lists.newArrayList();
        for (SysAcl acl : userAclList) {
            AclDto dto = AclDto.adapt(acl);
            dto.setHasAcl(true);
            dto.setChecked(true);
            aclDtoList.add(dto);
        }
        return aclListToTree(aclDtoList);
    }

    // 根据角色获取权限树
    public List<AclModuleLevelDto> roleTree(Integer roleId, Integer userId) {
        // 1,当前用户已分配的权限点
        List<SysAcl> userAclList = sysCoreService.getCurrentUserAclList(userId);
        // 2,当前角色分配的权限点
        List<SysAcl> roleAclList = sysCoreService.getRoleAclList(roleId);
        // 3,当前系统所有权限点
        List<AclDto> aclDtoList = Lists.newArrayList();

        // 用户权限id
        Set<Integer> userAclIdSet = userAclList.stream().map(sysAcl -> sysAcl.getId()).collect(Collectors.toSet());
        // 角色权限id
        Set<Integer> roleAclIdSet = roleAclList.stream().map(sysAcl -> sysAcl.getId()).collect(Collectors.toSet());

        // 所有权限点
        userAclList.addAll(roleAclList);

        List<SysAcl> allAclList = userAclList;
        List<SysAcl> allAcl = allAclList.stream().distinct().collect(Collectors.toList());
        for (SysAcl acl : allAcl) {
            AclDto dto = AclDto.adapt(acl);
            if (userAclIdSet.contains(acl.getId())) {
                dto.setHasAcl(true);
            }
            if (roleAclIdSet.contains(acl.getId())) {
                dto.setChecked(true);
            }
            aclDtoList.add(dto);
        }
        return aclListToTree(aclDtoList);
    }

    public List<AclModuleLevelDto> aclListToTree(List<AclDto> aclDtoList) {
        if (CollectionUtils.isEmpty(aclDtoList)) {
            return Lists.newArrayList();
        }
        // List<AclModuleLevelDto> aclModuleLevelList = aclModuleTree();
        List<AclModuleLevelDto> aclModuleLevelList = removeMenuManagementAclModuleTree(aclDtoList);
        // 根据权限点获取权限模块

        Multimap<Integer, AclDto> moduleIdAclMap = ArrayListMultimap.create();
        for (AclDto acl : aclDtoList) {
            if (acl.getStatus() == 1) {
                moduleIdAclMap.put(acl.getAclModuleId(), acl);
            }
        }
        bindAclsWithOrder(aclModuleLevelList, moduleIdAclMap);
        return aclModuleLevelList;
    }

    public List<AclModuleLevelDto> removeMenuManagementAclModuleTree(List<AclDto> aclDtoList) {

        List<Integer> aclIdSet = aclDtoList.stream().map(aclDto -> aclDto.getId()).collect(Collectors.toList());
        // 通过权限点id获取权限模块id
        List<Integer> aclModuleIdList = sysAclMapper.getAclModuleIdListByAclIdList(aclIdSet);
        if (CollectionUtils.isEmpty(aclModuleIdList)) {
            return Lists.newArrayList();
        }

        // 根据权限模块id获取权限模块
        List<SysAclModule> aclModuleList = sysAclModuleMapper.getByIdList(aclModuleIdList);

        StringBuilder builder = new StringBuilder();
        for (SysAclModule sysAclModule : aclModuleList) {
            builder.append(sysAclModule.getLevel()).append(".").append(sysAclModule.getId()).append(".");
        }
        // Level级别的list
        List<String> levelPartList = Arrays.asList(StringUtils.split(builder.toString(), "."));
        // 去重
        Set<String> set = new HashSet<>(levelPartList);
        // 当前登陆用户的权限模块id
        List<Integer> aclModuleIdLists = Lists.newArrayList();

        for (String levelPart : set) {
            aclModuleIdLists.add(Integer.valueOf(levelPart));
        }
        List<SysAclModule> currentUserAclModuleList = sysAclModuleMapper.getByIdList(aclModuleIdLists);

        List<AclModuleLevelDto> dtoList = Lists.newArrayList();
        for (SysAclModule aclModule : currentUserAclModuleList) {
            dtoList.add(AclModuleLevelDto.adapt(aclModule));
        }

        return aclModuleListToTree(dtoList);
    }

    // 将权限点绑定到权限模块下
    public void bindAclsWithOrder(List<AclModuleLevelDto> aclModuleLevelList,
                                  Multimap<Integer, AclDto> moduleIdAclMap) {
        if (CollectionUtils.isEmpty(aclModuleLevelList)) {
            return;
        }
        for (AclModuleLevelDto dto : aclModuleLevelList) {
            List<AclDto> aclDtoList = (List<AclDto>) moduleIdAclMap.get(dto.getId());
            if (CollectionUtils.isNotEmpty(aclDtoList)) {
                Collections.sort(aclDtoList, aclSeqComparator);
                dto.setAclList(aclDtoList);
            }
            bindAclsWithOrder(dto.getAclModuleList(), moduleIdAclMap);
        }

    }

    // 当前用户权限模块树
    public List<AclModuleLevelDto> userAclModuleTree(Integer userId) {
        if (sysCoreService.isSuperAdmin(userId)) {
            return aclModuleTree();
        }
        // 根据用户id获取角色id
        List<Integer> userRoleIdList = sysRoleUserMapper.getRoleIdListByUserId(userId);
        if (CollectionUtils.isEmpty(userRoleIdList)) {
            return Lists.newArrayList();
        }
        // 根据角色id获取权限点id

        // 根据角色id获取权限码
        List<String> codeList = sysRoleAclMapper.getCodeIdListByRoleIdList(userRoleIdList);
        if (CollectionUtils.isEmpty(codeList)) {
            return Lists.newArrayList();
        }

        // 通过权限码获取权限点id
        List<Integer> userAclIdList = sysAclMapper.getAclIdListByCodeList(codeList);

        if (CollectionUtils.isEmpty(userAclIdList)) {
            return Lists.newArrayList();
        }
        // 通过权限点id获取权限模块id
        List<Integer> aclModuleIdList = sysAclMapper.getAclModuleIdListByAclIdList(userAclIdList);
        if (CollectionUtils.isEmpty(aclModuleIdList)) {
            return Lists.newArrayList();
        }
        // 根据权限模块id获取权限模块
        List<SysAclModule> aclModuleList = sysAclModuleMapper.getByIdList(aclModuleIdList);

        StringBuilder builder = new StringBuilder();
        for (SysAclModule sysAclModule : aclModuleList) {
            builder.append(sysAclModule.getLevel()).append(".").append(sysAclModule.getId()).append(".");
        }
        // Level级别的list
        List<String> levelPartList = Arrays.asList(StringUtils.split(builder.toString(), "."));
        // 去重
        Set<String> set = new HashSet<>(levelPartList);
        // 当前登陆用户的权限模块id
        List<Integer> aclModuleIdLists = Lists.newArrayList();

        for (String levelPart : set) {
            aclModuleIdLists.add(Integer.valueOf(levelPart));
        }
        List<SysAclModule> currentUserAclModuleList = sysAclModuleMapper.getByIdList(aclModuleIdLists);

        List<AclModuleLevelDto> dtoList = Lists.newArrayList();
        for (SysAclModule aclModule : currentUserAclModuleList) {
            dtoList.add(AclModuleLevelDto.adapt(aclModule));
        }
        return aclModuleListToTree(dtoList);
    }

    // 权限模块树
    public List<AclModuleLevelDto> aclModuleTree() {
        List<SysAclModule> aclModuleList = sysAclModuleMapper.getAllAclModule();
        List<AclModuleLevelDto> dtoList = Lists.newArrayList();
        for (SysAclModule aclModule : aclModuleList) {
            dtoList.add(AclModuleLevelDto.adapt(aclModule));
        }
        return aclModuleListToTree(dtoList);
    }

    public List<AclModuleLevelDto> aclModuleListToTree(List<AclModuleLevelDto> dtoList) {
        if (CollectionUtils.isEmpty(dtoList)) {
            return Lists.newArrayList();
        }
        // level -> [aclmodule1, aclmodule2, ...] Map<String, List<Object>>
        Multimap<String, AclModuleLevelDto> levelAclModuleMap = ArrayListMultimap.create();
        List<AclModuleLevelDto> rootList = Lists.newArrayList();

        for (AclModuleLevelDto dto : dtoList) {
            levelAclModuleMap.put(dto.getLevel(), dto);
            if (LevelUtil.ROOT.equals(dto.getLevel())) {
                rootList.add(dto);
            }
        }
        Collections.sort(rootList, aclModuleSeqComparator);
        transformAclModuleTree(rootList, LevelUtil.ROOT, levelAclModuleMap);
        return rootList;

    }

    public void transformAclModuleTree(List<AclModuleLevelDto> dtoList, String level,
                                       Multimap<String, AclModuleLevelDto> levelAclModuleMap) {
        for (int i = 0; i < dtoList.size(); i++) {
            AclModuleLevelDto dto = dtoList.get(i);
            String nextLevel = LevelUtil.calculateLevel(level, dto.getId());
            List<AclModuleLevelDto> tempList = (List<AclModuleLevelDto>) levelAclModuleMap.get(nextLevel);
            if (CollectionUtils.isNotEmpty(tempList)) {
                Collections.sort(tempList, aclModuleSeqComparator);
                dto.setAclModuleList(tempList);
                transformAclModuleTree(tempList, nextLevel, levelAclModuleMap);
            }
        }
    }

    // 形成部门树
    public List<DeptLevelDto> deptTree() {
        // 所有部门
        List<SysDept> deptList = sysDeptMapper.getAllDept();
        List<DeptLevelDto> dtoList = Lists.newArrayList();
        for (SysDept dept : deptList) {
            DeptLevelDto dto = DeptLevelDto.adapt(dept);
            dtoList.add(dto);
        }
        return deptListToTree(dtoList);
    }

    // 将deptLevelList转换为树形结构
    public List<DeptLevelDto> deptListToTree(List<DeptLevelDto> deptLevelList) {
        if (CollectionUtils.isEmpty(deptLevelList)) {
            return Lists.newArrayList();
        }
        // 把当前的tree以它的level为key 把相同level的部门为value 放到map中
        // level->[dept1,dept2...]
        Multimap<String, DeptLevelDto> levelDeptMap = ArrayListMultimap.create();
        List<DeptLevelDto> rootList = Lists.newArrayList();
        for (DeptLevelDto dto : deptLevelList) {
            levelDeptMap.put(dto.getLevel(), dto);
            if (LevelUtil.ROOT.equals(dto.getLevel())) {
                rootList.add(dto);
            }
        }
        // 按照seq从小到大排序
        Collections.sort(rootList, new Comparator<DeptLevelDto>() {
            @Override
            public int compare(DeptLevelDto o1, DeptLevelDto o2) {
                return o1.getSeq() - o2.getSeq();
            }
        });
        // 递归生成树
        transformDeptTree(rootList, LevelUtil.ROOT, levelDeptMap);
        return rootList;
    }

    // 递归部门树
    public void transformDeptTree(List<DeptLevelDto> deptLevelList, String level,
                                  Multimap<String, DeptLevelDto> levelDeptMap) {
        for (int i = 0; i < deptLevelList.size(); i++) {
            // 遍历该层的每个元素
            DeptLevelDto deptLevelDto = deptLevelList.get(i);
            // 处理当前层级的数据
            String nextLevel = LevelUtil.calculateLevel(level, deptLevelDto.getId());
            // 处理下一层
            List<DeptLevelDto> tempDeptList = (List<DeptLevelDto>) levelDeptMap.get(nextLevel);
            if (CollectionUtils.isNotEmpty(tempDeptList)) {
                // 排序
                Collections.sort(tempDeptList, deptSeqComparator);
                // 设置下一层部门
                deptLevelDto.setDeptList(tempDeptList);
                // 进入到下一层处理
                transformDeptTree(tempDeptList, nextLevel, levelDeptMap);
            }
        }
    }

    public Comparator<DeptLevelDto> deptSeqComparator = new Comparator<DeptLevelDto>() {
        public int compare(DeptLevelDto o1, DeptLevelDto o2) {
            return o1.getSeq() - o2.getSeq();
        }
    };
    public Comparator<AclModuleLevelDto> aclModuleSeqComparator = new Comparator<AclModuleLevelDto>() {
        public int compare(AclModuleLevelDto o1, AclModuleLevelDto o2) {
            return o1.getSeq() - o2.getSeq();
        }
    };
    public Comparator<AclDto> aclSeqComparator = new Comparator<AclDto>() {
        public int compare(AclDto o1, AclDto o2) {
            return o1.getSeq() - o2.getSeq();
        }
    };
}
