package com.safecode.cyberzone.authorize.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.safecode.cyberzone.authorize.auth.AuthHolder;
import com.safecode.cyberzone.authorize.entity.SysAcl;
import com.safecode.cyberzone.authorize.entity.SysLog;
import com.safecode.cyberzone.authorize.entity.SysRole;
import com.safecode.cyberzone.authorize.entity.SysUser;
import com.safecode.cyberzone.authorize.param.AclParam;
import com.safecode.cyberzone.authorize.service.SysAclService;
import com.safecode.cyberzone.authorize.service.SysLogService;
import com.safecode.cyberzone.authorize.service.SysRoleService;
import com.safecode.cyberzone.authorize.utils.IpUtil;
import com.safecode.cyberzone.base.dto.ResponseData;

@RestController
@RequestMapping("/sys/acl")
public class SysAclController {

    @Autowired
    private SysAclService sysAclService;

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysLogService sysLogService;

    @Autowired
    private HttpServletRequest request;

    /*
     * 新增权限点
     */
    @PostMapping("/save")
    public ResponseData<SysAcl> saveAcl(@RequestBody AclParam param) {
        sysAclService.save(param);
        addLog("新增权限点:" + param.getName(), param.toString(), "/sys/acl/save");
        return new ResponseData<SysAcl>(HttpStatus.OK.value(), "新增成功", null);
    }

    /*
     * 修改权限点
     */
    @PostMapping("/update")
    public ResponseData<SysAcl> updateAcl(@RequestBody AclParam param) {
        sysAclService.update(param);
        addLog("修改权限点:" + param.getName(), param.toString(), "/sys/acl/update");
        return new ResponseData<SysAcl>(HttpStatus.OK.value(), "修改成功", null);
    }

    /*
     * 根据权限模块获取权限点
     */
    @PostMapping("/page")
    public ResponseData<PageInfo<SysAcl>> page(@RequestParam("aclModuleId") Integer aclModuleId,
                                               @PageableDefault(page = 1, size = 10, sort = "id,asc") Pageable pageable) {
        PageInfo<SysAcl> pageInfo = sysAclService.getPageByAclModuleId(aclModuleId, pageable);
        return new ResponseData<PageInfo<SysAcl>>(HttpStatus.OK.value(), "查询成功", pageInfo);
    }

    /*
     * 获取权限点下所有的角色和用户
     */
    @JsonView(SysUser.SysUserSimpleView.class)
    @PostMapping("/acls")
    public ResponseData<Map<String, Object>> acls(@RequestParam("aclId") int aclId) {
        Map<String, Object> map = Maps.newHashMap();
        List<SysRole> roleList = sysRoleService.getRoleListByAclId(aclId);
        map.put("roles", roleList);
        map.put("users", sysRoleService.getUserListByRoleList(roleList));
        return new ResponseData<Map<String, Object>>(HttpStatus.OK.value(), "查询成功", map);
    }

    @PostMapping("/id")
    public ResponseData<SysAcl> id(@RequestParam("aclId") int aclId) {
        SysAcl sysAcl = sysAclService.selectByPrimaryKey(aclId);
        return new ResponseData<SysAcl>(HttpStatus.OK.value(), "查询成功", sysAcl);
    }

    /*
     * 删除权限点
     */
    @PostMapping("/delete")
    public ResponseData<SysAcl> delete(@RequestParam("id") int id) {
        SysAcl acl = sysAclService.selectByPrimaryKey(id);
        sysAclService.delete(id);
        addLog("删除权限点:" + acl.getName(), String.valueOf(id), "/sys/acl/delete");
        return new ResponseData<SysAcl>(HttpStatus.OK.value(), "删除成功", null);
    }

    private void addLog(String remark, String requestObject, String requestUrl) {
        sysLogService.save(SysLog.builder().remark(remark).userId(AuthHolder.getCurrentUserId().longValue())
                .projectName("cyberzone-authorize").requestObject(requestObject).requestUrl(requestUrl)
                .createTime(new Date()).userName(AuthHolder.getCurrentUsername()).ip(IpUtil.getRemoteIp(request))
                .logStatus("操作日志").build());
    }
}
