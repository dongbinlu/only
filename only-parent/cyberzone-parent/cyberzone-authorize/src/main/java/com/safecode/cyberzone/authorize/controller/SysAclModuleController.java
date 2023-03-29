package com.safecode.cyberzone.authorize.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.safecode.cyberzone.authorize.dto.AclModuleLevelDto;
import com.safecode.cyberzone.authorize.entity.SysAclModule;
import com.safecode.cyberzone.authorize.mapper.SysAclModuleMapper;
import com.safecode.cyberzone.authorize.param.AclModuleParam;
import com.safecode.cyberzone.authorize.service.SysAclModuleService;
import com.safecode.cyberzone.authorize.service.impl.SysTreeService;
import com.safecode.cyberzone.base.dto.ResponseData;

@RestController
@RequestMapping("/sys/aclModule")
public class SysAclModuleController {

    @Autowired
    private SysAclModuleService sysAclModuleService;

    @Autowired
    private SysTreeService sysTreeService;

    @Autowired
    private SysAclModuleMapper sysAclModuleMapper;

    /*
     * 新增权限模块
     */
    @PostMapping("/save")
    public ResponseData<SysAclModule> saveAclModule(@RequestBody AclModuleParam param) {
        sysAclModuleService.save(param);
        return new ResponseData<SysAclModule>(HttpStatus.OK.value(), "保存成功", null);
    }

    /*
     * 修改权限模块
     */
    @PostMapping("/update")
    public ResponseData<SysAclModule> updateAclModule(@RequestBody AclModuleParam param) {
        sysAclModuleService.update(param);
        return new ResponseData<SysAclModule>(HttpStatus.OK.value(), "修改成功", null);
    }

    /*
     * 权限模块树
     */
    @GetMapping("/tree")
    public ResponseData<List<AclModuleLevelDto>> tree() {
        List<AclModuleLevelDto> aclModuleTree = sysTreeService.aclModuleTree();
        return new ResponseData<List<AclModuleLevelDto>>(HttpStatus.OK.value(), "查询成功", aclModuleTree);
    }

    /*
     * 删除权限模块
     */
    @PostMapping("/delete")
    public ResponseData<SysAclModule> delete(@RequestParam("id") int id) {
        sysAclModuleService.delete(id);
        return new ResponseData<SysAclModule>(HttpStatus.OK.value(), "删除成功", null);
    }
}
