package com.safecode.security.permission.service.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.safecode.security.permission.dto.AclDto;
import com.safecode.security.permission.dto.AclModuleLevelDto;
import com.safecode.security.permission.dto.DeptLevelDto;
import com.safecode.security.permission.entity.SysAcl;
import com.safecode.security.permission.entity.SysAclModule;
import com.safecode.security.permission.entity.SysDept;
import com.safecode.security.permission.mapper.SysAclMapper;
import com.safecode.security.permission.mapper.SysAclModuleMapper;
import com.safecode.security.permission.mapper.SysDeptMapper;
import com.safecode.security.permission.utils.LevelUtil;

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

    public List<AclModuleLevelDto> userAclTree(Integer userId) {

        // 1，获取用户的权限点
        List<SysAcl> userAclList = sysCoreService.getUserAclList(userId);

        // 2,适配数据
        List<AclDto> aclDtoList = Lists.newArrayList();
        for (SysAcl acl : userAclList) {
            AclDto dto = AclDto.adatp(acl);
            dto.setChecked(true);
            dto.setHasAcl(true);
            aclDtoList.add(dto);
        }

        return aclListToTree(aclDtoList);
    }

    /**
     * 角色权限树
     *
     * @param roleId
     * @return
     */
    public List<AclModuleLevelDto> roleTree(int roleId) {
        // 1,当前用户已经分配的权限点
        List<SysAcl> userAclList = sysCoreService.getCurrentUserAclList();

        // 2,当前角色分配的权限点
        List<SysAcl> roleAclList = sysCoreService.getRoleAclList(roleId);

        // 3,当前系统所有的权限点
        List<AclDto> aclDtoList = Lists.newArrayList();

        Set<Integer> userAclIdSet = userAclList.stream().map(sysAcl -> sysAcl.getId()).collect(Collectors.toSet());

        Set<Integer> roleAclIdSet = roleAclList.stream().map(sysAcl -> sysAcl.getId()).collect(Collectors.toSet());

        // 获取系统所有权限点
        List<SysAcl> allAclList = sysAclMapper.getAll();
        for (SysAcl acl : allAclList) {
            AclDto dto = AclDto.adatp(acl);
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

        List<AclModuleLevelDto> aclModuleLevelList = aclModuleTree();

        // 以aclModuleId为key，以相同的aclModuleId的权限点为value
        Multimap<Integer, AclDto> moduleIdAclMap = ArrayListMultimap.create();

        for (AclDto acl : aclDtoList) {
            if (acl.getStatus() == 1) {
                moduleIdAclMap.put(acl.getAclModuleId(), acl);
            }
        }

        bindAclsWithOrder(aclModuleLevelList, moduleIdAclMap);
        return aclModuleLevelList;
    }

    private void bindAclsWithOrder(List<AclModuleLevelDto> aclModuleLevelList,
                                   Multimap<Integer, AclDto> moduleIdAclMap) {

        if (CollectionUtils.isEmpty(aclModuleLevelList)) {
            return;
        }

        for (AclModuleLevelDto dto : aclModuleLevelList) {
            List<AclDto> aclDtoList = (List<AclDto>) moduleIdAclMap.get(dto.getId());
            if (CollectionUtils.isNotEmpty(aclDtoList)) {
                // 排序
                Collections.sort(aclDtoList, aclSeqComparator);
                dto.setAclList(aclDtoList);
            }
            bindAclsWithOrder(dto.getAclModuleList(), moduleIdAclMap);
        }

    }

    /**
     * 权限模块树
     *
     * @return
     */
    public List<AclModuleLevelDto> aclModuleTree() {

        List<SysAclModule> aclModuleList = sysAclModuleMapper.getAll();
        if (CollectionUtils.isEmpty(aclModuleList)) {
            return Lists.newArrayList();
        }
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

        Multimap<String, AclModuleLevelDto> aclMoluleLevelMap = ArrayListMultimap.create();

        List<AclModuleLevelDto> rootList = Lists.newArrayList();

        for (AclModuleLevelDto aclModuleLevelDto : dtoList) {
            aclMoluleLevelMap.put(aclModuleLevelDto.getLevel(), aclModuleLevelDto);
            if (LevelUtil.ROOT.equals(aclModuleLevelDto.getLevel())) {
                rootList.add(aclModuleLevelDto);
            }
        }
        // 排序
        Collections.sort(rootList, aclModuleSeqComparator);
        transformAclModuleTree(rootList, LevelUtil.ROOT, aclMoluleLevelMap);
        return rootList;

    }

    public void transformAclModuleTree(List<AclModuleLevelDto> dtoList, String level,
                                       Multimap<String, AclModuleLevelDto> aclMoluleLevelMap) {

        for (int i = 0; i < dtoList.size(); i++) {
            AclModuleLevelDto dto = dtoList.get(i);
            String nextLevel = LevelUtil.calculateLevel(level, dto.getId());
            List<AclModuleLevelDto> tempList = (List<AclModuleLevelDto>) aclMoluleLevelMap.get(nextLevel);
            if (CollectionUtils.isNotEmpty(tempList)) {
                // 排序
                Collections.sort(tempList, aclModuleSeqComparator);
                dto.setAclModuleList(tempList);
                transformAclModuleTree(tempList, nextLevel, aclMoluleLevelMap);
            }
        }

    }

    /**
     * 部门树
     *
     * @return
     */
    public List<DeptLevelDto> deptTree() {

        List<SysDept> deptList = sysDeptMapper.getAll();

        List<DeptLevelDto> dtoList = Lists.newArrayList();

        if (CollectionUtils.isEmpty(deptList)) {
            return Lists.newArrayList();
        }
        for (SysDept dept : deptList) {
            DeptLevelDto dto = DeptLevelDto.adapt(dept);
            dtoList.add(dto);
        }
        return deptListToTree(dtoList);
    }

    /**
     * 将List<DeptLevelDto>转化为树形结构
     *
     * @param deptLevelList
     * @return
     */
    public List<DeptLevelDto> deptListToTree(List<DeptLevelDto> deptLevelList) {
        if (CollectionUtils.isEmpty(deptLevelList)) {
            return Lists.newArrayList();
        }

        // 创建Multimap
        // level->[dept,dept] 0.24->[前端,后端]
        // 以level为key，相同level的dto作为Value
        Multimap<String, DeptLevelDto> levelDeptMap = ArrayListMultimap.create();

        // 创建根节点
        List<DeptLevelDto> rootList = Lists.newArrayList();

        // 循环遍历deptLevelList map 里面的值为{0=[技术部],0.24=[前端，后端]}
        for (DeptLevelDto dto : deptLevelList) {
            levelDeptMap.put(dto.getLevel(), dto);
            if (LevelUtil.ROOT.equals(dto.getLevel())) {
                rootList.add(dto);
            }
        }
        // 对rootList排序，按照seq从小到大排序
        Collections.sort(rootList, deptSeqComparator);

        // 递归生成树
        transformDeptTree(rootList, LevelUtil.ROOT, levelDeptMap);
        return rootList;
    }

    // 递归树的生成
    public void transformDeptTree(List<DeptLevelDto> deptLevelList, String level,
                                  Multimap<String, DeptLevelDto> levelDeptMap) {
        for (int i = 0; i < deptLevelList.size(); i++) {
            // 循环遍历该层的每一个元素
            DeptLevelDto deptLevelDto = deptLevelList.get(i); // 技术部
            // 处理当前层级的数据
            String nextLevel = LevelUtil.calculateLevel(level, deptLevelDto.getId()); // 0.24
            // 处理下一层
            List<DeptLevelDto> tempDeptList = (List<DeptLevelDto>) levelDeptMap.get(nextLevel); // [前端，后端]

            if (CollectionUtils.isNotEmpty(tempDeptList)) {
                // 排序
                Collections.sort(tempDeptList, deptSeqComparator);
                // 设置下一层部门
                deptLevelDto.setDeptList(tempDeptList); // 将前端和后端挂在技术部下
                // 进入到下一层处理
                transformDeptTree(tempDeptList, nextLevel, levelDeptMap);
            }
        }
    }

    // 部门 比较器
    public Comparator<DeptLevelDto> deptSeqComparator = new Comparator<DeptLevelDto>() {
        @Override
        public int compare(DeptLevelDto o1, DeptLevelDto o2) {
            return o1.getSeq() - o2.getSeq();
        }
    };

    // 权限模块比较器
    public Comparator<AclModuleLevelDto> aclModuleSeqComparator = new Comparator<AclModuleLevelDto>() {

        @Override
        public int compare(AclModuleLevelDto o1, AclModuleLevelDto o2) {
            return o1.getSeq() - o2.getSeq();
        }
    };
    // 权限点比较器
    public Comparator<AclDto> aclSeqComparator = new Comparator<AclDto>() {
        @Override
        public int compare(AclDto o1, AclDto o2) {
            return o1.getSeq() - o2.getSeq();
        }
    };

}
