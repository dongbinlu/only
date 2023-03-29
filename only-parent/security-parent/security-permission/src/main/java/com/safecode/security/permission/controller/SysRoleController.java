package com.safecode.security.permission.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.safecode.security.permission.common.JsonData;
import com.safecode.security.permission.entity.SysUser;
import com.safecode.security.permission.param.RoleParam;
import com.safecode.security.permission.service.SysRoleService;
import com.safecode.security.permission.service.SysUserService;
import com.safecode.security.permission.service.impl.SysRoleAclService;
import com.safecode.security.permission.service.impl.SysRoleUserService;
import com.safecode.security.permission.service.impl.SysTreeService;
import com.safecode.security.permission.utils.StringUtil;

@RestController
@RequestMapping("/sys/role")
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysTreeService sysTreeService;

    @Autowired
    private SysRoleAclService sysRoleAclService;

    @Autowired
    private SysRoleUserService sysRoleUserService;

    @Autowired
    private SysUserService sysUserService;

    /**
     * 新增角色
     *
     * @param param
     * @return
     */
    @PostMapping("/save")
    public JsonData saveRole(RoleParam param) {
        sysRoleService.save(param);
        return JsonData.success();
    }

    /**
     * 修改角色
     *
     * @param param
     * @return
     */
    @PostMapping("/update")
    public JsonData updateRole(RoleParam param) {
        sysRoleService.update(param);
        return JsonData.success();
    }

    /**
     * <p>Title: deleteRole</p>
     * <p>Description: 删除角色</p>
     *
     * @param roleId
     * @return
     */
    @PostMapping("/delete")
    public JsonData deleteRole(@RequestParam("roleId") Integer roleId) {
        sysRoleService.delete(roleId);
        return JsonData.success();
    }

    /**
     * 获取角色列表
     *
     * @param pageable
     * @return
     */
    @PostMapping("/page")
    public JsonData page(
            @PageableDefault(page = 1, size = 10, sort = "operate_time", direction = Direction.ASC) Pageable pageable) {
        return JsonData.success(sysRoleService.getPage(pageable));
    }

    /**
     * 根据角色id获取权限树 权限模块里面嵌套权限点
     * <p>
     * 根据角色id查看哪些权限是否勾选
     *
     * @param roleId
     * @return
     */
    @PostMapping("/roleTree")
    public JsonData roleTree(@RequestParam("roleId") int roleId) {
        return JsonData.success(sysTreeService.roleTree(roleId));
    }

    /**
     * 根据角色Id修改权限(角色授权)
     *
     * @param roleId
     * @param codeId 权限码用逗号分隔
     * @return
     */
    @PostMapping("/changeAcls")
    public JsonData changeAcls(@RequestParam("roleId") int roleId,
                               @RequestParam(value = "codeIds", required = false, defaultValue = "") String codeIds) {
        sysRoleAclService.changeRoleAcls(roleId, StringUtil.splitToListString(codeIds));
        return JsonData.success();

    }

    /**
     * 修改角色对应的用户，角色用户
     *
     * @param roleId
     * @param userIds
     * @return
     */
    @PostMapping("/changeUsers")
    public JsonData changeUsers(@RequestParam("roleId") int roleId,
                                @RequestParam(value = "userIds", defaultValue = "", required = false) String userIds) {
        List<Integer> userIdList = StringUtil.splitToListInt(userIds);
        sysRoleUserService.changeRoleUsers(roleId, userIdList);
        return JsonData.success();
    }

    /**
     * 根据角色获取已选中和未选中的用户 已选中的不管状态是多少，直接返回已选中的 未选中的需要判断用户状态
     *
     * @param roleId
     * @return
     */
    @PostMapping("/users")
    public JsonData users(@RequestParam("roleId") int roleId) {

        List<SysUser> selectedUserList = sysRoleUserService.getListByRoleId(roleId);

        List<SysUser> allUserList = sysUserService.getAll();

        List<SysUser> unSelectedUserList = Lists.newArrayList();

        Set<Integer> selectedUserSet = selectedUserList.stream().map(sysUser -> sysUser.getId())
                .collect(Collectors.toSet());
        for (SysUser sysUser : allUserList) {
            if (sysUser.getStatus() == 1 && !selectedUserSet.contains(sysUser.getId())) {
                unSelectedUserList.add(sysUser);
            }
        }

        // 如果需要将已选中的用户也区分可用状态，需加一层过滤再包装,将状态不等于1的过滤掉
        List<SysUser> selectedUserStatusTO1List = selectedUserList.stream().filter(sysUser -> sysUser.getStatus() == 1)
                .collect(Collectors.toList());

        Map<String, List<SysUser>> map = Maps.newHashMap();
        map.put("selected", selectedUserList);
        map.put("unSelected", unSelectedUserList);
        map.put("selectedStatusTO1", selectedUserStatusTO1List);
        return JsonData.success(map);

    }
}
