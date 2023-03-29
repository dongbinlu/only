package com.safecode.cyberzone.authorize.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.safecode.cyberzone.authorize.auth.AuthHolder;
import com.safecode.cyberzone.authorize.dto.AclModuleLevelDto;
import com.safecode.cyberzone.authorize.dto.RoleDto;
import com.safecode.cyberzone.authorize.entity.SysAcl;
import com.safecode.cyberzone.authorize.entity.SysLog;
import com.safecode.cyberzone.authorize.entity.SysRole;
import com.safecode.cyberzone.authorize.entity.SysUser;
import com.safecode.cyberzone.authorize.mapper.SysRoleMapper;
import com.safecode.cyberzone.authorize.param.RoleParam;
import com.safecode.cyberzone.authorize.service.SysLogService;
import com.safecode.cyberzone.authorize.service.SysRoleService;
import com.safecode.cyberzone.authorize.service.SysUserService;
import com.safecode.cyberzone.authorize.service.impl.SysRoleAclService;
import com.safecode.cyberzone.authorize.service.impl.SysRoleUserService;
import com.safecode.cyberzone.authorize.service.impl.SysTreeService;
import com.safecode.cyberzone.authorize.utils.IpUtil;
import com.safecode.cyberzone.authorize.utils.StringUtil;
import com.safecode.cyberzone.base.dto.ResponseData;

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

    @Autowired
    private SysRoleMapper sysRoleMapper;

    @Autowired
    private SysLogService sysLogService;

    @Autowired
    private HttpServletRequest request;

    /*
     * 新增角色
     */
    @PostMapping("/save")
    public ResponseData<SysRole> saveRole(@RequestBody RoleParam param) {
        sysRoleService.save(param);
        addLog("新增角色:" + param.getName(), param.toString(), "/sys/role/save");
        return new ResponseData<SysRole>(HttpStatus.OK.value(), "新增成功", null);
    }

    /*
     * 修改角色
     */
    @PostMapping("/update")
    public ResponseData<SysRole> updateRole(@RequestBody RoleParam param) {
        sysRoleService.update(param);
        addLog("修改角色:" + param.getName(), param.toString(), "/sys/role/update");
        return new ResponseData<SysRole>(HttpStatus.OK.value(), "修改成功", null);
    }

    /*
     * 获取角色列表
     */
    @PostMapping("/list")
    public ResponseData<PageInfo<RoleDto>> list(
            @PageableDefault(page = 1, size = 10, sort = "operate_time", direction = Direction.DESC) Pageable pageable,
            RoleDto dto) {
        return new ResponseData<PageInfo<RoleDto>>(HttpStatus.OK.value(), "查询成功",
                sysRoleService.newGetAll(pageable, dto));
    }

    @PostMapping("/id")
    public ResponseData<SysRole> id(@RequestParam("roleId") Integer roleId) {
        return new ResponseData<SysRole>(HttpStatus.OK.value(), "查询成功", sysRoleService.id(roleId));
    }

    /*
     * 取出当前角色的权限树
     */
    @PostMapping("/roleTree")
    public ResponseData<List<AclModuleLevelDto>> roleTree(@RequestParam("roleId") Integer roleId) {
        Integer currentUserId = AuthHolder.getCurrentUserId();
        List<AclModuleLevelDto> roleTree = sysTreeService.roleTree(roleId, currentUserId);
        return new ResponseData<List<AclModuleLevelDto>>(HttpStatus.OK.value(), "查询成功", roleTree);
    }

    /*
     * 根据角色ID更新权限
     */
    @PostMapping("/changeAcls")
    public ResponseData<SysAcl> changeAcls(@RequestParam("roleId") int roleId,
                                           @RequestParam(value = "aclCodes", required = false, defaultValue = "") String aclCodes) {
        List<String> aclCodeList = StringUtil.splitToListString(aclCodes);
        SysRole role = sysRoleMapper.selectByPrimaryKey(roleId);
        sysRoleAclService.changRoleAcls(roleId, aclCodeList);
        // 执行端角色同步REDIS
        sysRoleService.writeExecuteRoleIdToRedis();
        addLog("根据角色ID更新权限-角色名称为:" + role.getName(), String.valueOf(roleId) + "," + aclCodes, "/sys/role/changeAcls");
        return new ResponseData<SysAcl>(HttpStatus.OK.value(), "修改成功", null);
    }

    /*
     * 根据角色Id更新用户
     */
    @PostMapping("/changeUsers")
    public ResponseData<SysUser> changeUsers(@RequestParam("roleId") int roleId,
                                             @RequestParam(value = "userIds", required = false, defaultValue = "") String userIds) {
        List<Integer> userIdList = StringUtil.splitToListInt(userIds);
        SysRole role = sysRoleMapper.selectByPrimaryKey(roleId);
        sysRoleUserService.changeRoleUsers(roleId, userIdList);
        addLog("根据角色ID更新用户-角色名称为:" + role.getName(), String.valueOf(roleId) + "," + userIds, "/sys/role/changeUsers");
        return new ResponseData<SysUser>(HttpStatus.OK.value(), "修改成功", null);
    }

    /*
     * 根据角色id获取已选中和未选中的用户
     */

    @JsonView(SysUser.SysUserSimpleView.class)
    @PostMapping("/users")
    public ResponseData<Map<String, List<SysUser>>> users(@RequestParam("roleId") int roleId) {
        List<SysUser> selectedUserList = sysRoleUserService.getListByRoleId(roleId);

        List<SysUser> allUserList = sysUserService.getAll();

        List<SysUser> unSelectedUserList = Lists.newArrayList();

        // 加速 流式遍历 map操作 取出SysUser 对应的值是用户id 生成set列表
        Set<Integer> selectUserIdSet = selectedUserList.stream().map(sysUser -> sysUser.getId())
                .collect(Collectors.toSet());

        for (SysUser sysUser : allUserList) {
            if (sysUser.getStatus() == 1 && !selectUserIdSet.contains(sysUser.getId())) {
                unSelectedUserList.add(sysUser);
            }
        }

        Map<String, List<SysUser>> map = Maps.newHashMap();
        map.put("selected", selectedUserList);
        map.put("unSelected", unSelectedUserList);
        return new ResponseData<Map<String, List<SysUser>>>(HttpStatus.OK.value(), "查询成功", map);
    }

    /*
     * 角色对应的用户，已选中的
     */

    @JsonView(SysUser.SysUserSimpleView.class)
    @PostMapping("/roleToUsers")
    public ResponseData<PageInfo<SysUser>> roleToUsers(
            @PageableDefault(page = 1, size = 10, sort = "operate_time", direction = Direction.DESC) Pageable pageable,
            @RequestParam("roleId") int roleId, String account, String username) {
        PageInfo<SysUser> page = sysRoleService.roleToUser(pageable, roleId, account, username);
        return new ResponseData<PageInfo<SysUser>>(HttpStatus.OK.value(), "查询成功", page);
    }

    @PostMapping("/remove")
    // 移除用户，移除指定角色下的用户
    public ResponseData<Object> removeRoleUser(@RequestParam("roleId") Integer roleId,
                                               @RequestParam("userId") Integer userId) {
        sysRoleService.remove(roleId, userId);
        SysRole role = sysRoleMapper.selectByPrimaryKey(roleId);
        addLog("移除指定角色下的用户-角色名称为:" + role.getName(), String.valueOf(roleId) + "," + userId, "/sys/role/remove");
        return new ResponseData<Object>(HttpStatus.OK.value(), "移除成功", null);

    }

    @PostMapping("/removes")
    // 批量移除用户，移除指定角色下的用户
    public ResponseData<Object> removesRoleUser(@RequestParam("roleId") Integer roleId,
                                                @RequestParam("userIds") String userIds) {
        sysRoleService.batchRemove(roleId, userIds);
        SysRole role = sysRoleMapper.selectByPrimaryKey(roleId);
        addLog("批量移除指定角色下的用户-角色名称为:" + role.getName(), String.valueOf(roleId) + "," + userIds,
                "/sys/role/removesRoleUser");
        return new ResponseData<Object>(HttpStatus.OK.value(), "移除成功", null);

    }

    /*
     * 删除角色
     */

    @PostMapping("/delete")
    public ResponseData<SysRole> delete(@RequestParam("id") int id) {
        SysRole role = sysRoleMapper.selectByPrimaryKey(id);
        sysRoleService.delete(id);
        addLog("删除角色:" + role.getName(), String.valueOf(id), "/sys/role/delete");
        return new ResponseData<SysRole>(HttpStatus.OK.value(), "删除成功", null);
    }

    /*
     * 批量删除
     */

    @PostMapping("/deletes")
    public ResponseData<SysRole> deletes(@RequestParam("roleIds") String roleIds) {
        List<Integer> roleIdList = StringUtil.splitToListInt(roleIds);
        sysRoleService.batchDelete(roleIdList);
        addLog("批量删除角色-删除的角色ID为:" + roleIds, roleIds, "/sys/role/deletes");
        return new ResponseData<SysRole>(HttpStatus.OK.value(), "删除成功", null);
    }

    /*
     * 成员管理页面-获取平台管理员
     */
    @GetMapping("/admin")
    public ResponseData<Object> getPlatformAdmin() {
        List<SysRole> sysRoles = sysRoleService.getRoleListByUserId(AuthHolder.getCurrentUserId());
        List<String> list = sysRoles.stream().map(sysRole -> sysRole.getAdmin()).collect(Collectors.toList());
        list = Arrays.asList(String.join(",", list).split(",")).stream().distinct().collect(Collectors.toList());
        return new ResponseData<Object>(HttpStatus.OK.value(), "查询成功", String.join(" ", list));
    }

    private void addLog(String remark, String requestObject, String requestUrl) {
        sysLogService.save(SysLog.builder().remark(remark).userId(AuthHolder.getCurrentUserId().longValue())
                .projectName("cyberzone-authorize").requestObject(requestObject).requestUrl(requestUrl)
                .createTime(new Date()).userName(AuthHolder.getCurrentUsername()).ip(IpUtil.getRemoteIp(request))
                .logStatus("操作日志").build());
    }
}
