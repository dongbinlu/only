package com.safecode.security.permission.controller;

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
import com.safecode.security.permission.param.UserParam;
import com.safecode.security.permission.service.SysRoleService;
import com.safecode.security.permission.service.SysUserService;
import com.safecode.security.permission.service.impl.SysRoleUserService;
import com.safecode.security.permission.service.impl.SysTreeService;
import com.safecode.security.permission.utils.StringUtil;

@RestController
@RequestMapping("/sys/user")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysRoleUserService sysRoleUserService;

    @Autowired
    private SysTreeService sysTreeService;

    @Autowired
    private SysRoleService sysRoleService;

    /**
     * 新增用户
     *
     * @param param
     * @return
     */
    @PostMapping("/save")
    public JsonData saveUser(UserParam param) {
        sysUserService.save(param);
        return JsonData.success();
    }

    /**
     * 修改用户
     *
     * @param param
     * @return
     */

    @PostMapping("/update")
    public JsonData updateUser(UserParam param) {
        sysUserService.update(param);
        return JsonData.success();
    }

    @PostMapping("/delete")
    public JsonData deleteUser(@RequestParam("userId") Integer userId) {
        sysUserService.delete(userId);
        return JsonData.success();
    }

    /**
     * 获取指定部门用户列表，默认获取所有用户列表，分页显示
     *
     * @param deptId
     * @param pageable
     * @return
     */
    @PostMapping("/page")
    public JsonData page(@RequestParam(value = "deptId", defaultValue = "") Integer deptId,
                         @PageableDefault(page = 1, size = 10, sort = "operate_time", direction = Direction.ASC) Pageable pageable) {
        return JsonData.success(sysUserService.getPageByDeptId(deptId, pageable));
    }

    /**
     * 修改用户指定的角色，用户角色授权
     *
     * @param userId
     * @param roleIds
     * @return
     */
    @PostMapping("/changeRoles")
    public JsonData changeRoles(@RequestParam("userId") int userId,
                                @RequestParam(value = "roleIds", defaultValue = "", required = false) String roleIds) {
        sysRoleUserService.changeUserRoles(userId, StringUtil.splitToListInt(roleIds));
        return JsonData.success();
    }

    /**
     * 获取指定用户的权限树和角色列表
     *
     * @param userId
     * @return
     */
    @PostMapping("/acls")
    public JsonData acls(@RequestParam("userId") int userId) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("acls", sysTreeService.userAclTree(userId));
        map.put("roles", sysRoleService.getRoleListByUserId(userId));
        return JsonData.success(map);
    }

}
