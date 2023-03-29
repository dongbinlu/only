package com.safecode.security.permission.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.safecode.security.permission.common.JsonData;
import com.safecode.security.permission.param.AclModuleParam;
import com.safecode.security.permission.service.SysAclModuleService;
import com.safecode.security.permission.service.impl.SysTreeService;

@RestController
@RequestMapping("/sys/aclModule")
public class SysAclModuleController {

    @Autowired
    private SysAclModuleService sysAclModuleService;

    @Autowired
    private SysTreeService sysTreeService;

    /**
     * 新增权限模块
     *
     * @param param
     * @return
     */
    @PostMapping("/save")
    public JsonData saveAclModule(AclModuleParam param) {
        sysAclModuleService.save(param);
        return JsonData.success();
    }

    /**
     * 更新权限模块
     *
     * @param param
     * @return
     */
    @PostMapping("/update")
    public JsonData updateAclModule(AclModuleParam param) {
        sysAclModuleService.update(param);
        return JsonData.success();
    }

    /**
     * 权限模块树
     *
     * @return
     */
    @GetMapping("/tree")
    public JsonData tree() {
        return JsonData.success(sysTreeService.aclModuleTree());
    }

    /**
     * 删除权限模块
     *
     * @param id
     * @return
     */
    @PostMapping("/delete")
    public JsonData delete(@RequestParam("id") int id) {
        sysAclModuleService.delete(id);
        return JsonData.success();
    }
}
