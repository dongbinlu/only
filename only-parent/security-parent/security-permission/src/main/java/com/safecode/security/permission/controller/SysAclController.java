package com.safecode.security.permission.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Maps;
import com.safecode.security.permission.common.JsonData;
import com.safecode.security.permission.entity.SysRole;
import com.safecode.security.permission.param.AclParam;
import com.safecode.security.permission.service.SysAclService;
import com.safecode.security.permission.service.SysRoleService;

@RestController
@RequestMapping("/sys/acl")
public class SysAclController {

    @Autowired
    private SysAclService sysAclService;

    @Autowired
    private SysRoleService sysRoleService;

    @PostMapping("/save")
    public JsonData saveAcl(AclParam param) {
        sysAclService.save(param);
        return JsonData.success();
    }

    @PostMapping("/update")
    public JsonData updateAcl(AclParam param) {
        sysAclService.update(param);
        return JsonData.success();
    }

    /**
     * 根据权限模块获取权限点
     *
     * @param aclModuleId
     * @param pageable
     * @return
     */
    @PostMapping("/page")
    public JsonData page(@RequestParam("aclModuleId") Integer aclModuleId,
                         @PageableDefault(page = 1, size = 10, sort = "operate_time", direction = Direction.ASC) Pageable pageable) {
        return JsonData.success(sysAclService.getPageByAclModuleId(aclModuleId, pageable));
    }

    /**
     * 删除权限点
     *
     * @param id
     * @return
     */
    @PostMapping("/delete")
    public JsonData delete(@RequestParam("id") int id) {
        sysAclService.delete(id);
        return JsonData.success();
    }

    /**
     * 获取权限点分配的用户和角色
     *
     * @return
     */
    @PostMapping("/acls")
    public JsonData acls(@RequestParam("aclId") int aclId) {

        Map<String, Object> map = Maps.newHashMap();
        List<SysRole> roleList = sysRoleService.getRoleListByAclId(aclId);
        map.put("roles", roleList);
        map.put("users", sysRoleService.getUserListByRoleList(roleList));
        return JsonData.success(map);
    }
}
