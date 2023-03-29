package com.safecode.cyberzone.authorize.controller;

import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonView;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.safecode.cyberzone.authorize.auth.AuthHolder;
import com.safecode.cyberzone.authorize.dto.UserDto;
import com.safecode.cyberzone.authorize.entity.SysAcl;
import com.safecode.cyberzone.authorize.entity.SysLog;
import com.safecode.cyberzone.authorize.entity.SysRole;
import com.safecode.cyberzone.authorize.entity.SysUser;
import com.safecode.cyberzone.authorize.param.ImportParam;
import com.safecode.cyberzone.authorize.param.UserAdminParam;
import com.safecode.cyberzone.authorize.param.UserAdminPsParam;
import com.safecode.cyberzone.authorize.param.UserCenterParam;
import com.safecode.cyberzone.authorize.param.UserNoLoPsParam;
import com.safecode.cyberzone.authorize.param.UserParam;
import com.safecode.cyberzone.authorize.param.UserPsParam;
import com.safecode.cyberzone.authorize.service.SysLogService;
import com.safecode.cyberzone.authorize.service.SysRoleService;
import com.safecode.cyberzone.authorize.service.SysUserService;
import com.safecode.cyberzone.authorize.service.impl.SysCoreService;
import com.safecode.cyberzone.authorize.service.impl.SysRoleUserService;
import com.safecode.cyberzone.authorize.service.impl.SysTreeService;
import com.safecode.cyberzone.authorize.utils.IpUtil;
import com.safecode.cyberzone.authorize.utils.StringUtil;
import com.safecode.cyberzone.base.dto.ResponseData;
import com.safecode.cyberzone.base.utils.CodeUtils;

@RestController
@RequestMapping("/sys/user")
public class SysUserController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysTreeService sysTreeService;

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysCoreService sysCoreService;

    @Autowired
    private SysRoleUserService sysRoleUserService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private SysLogService sysLogService;

    /*
     * 新增用户
     */
    @PostMapping("/save")
    public ResponseData<Integer> saveUser(@RequestBody UserParam param) {
        SysUser sysUser = sysUserService.save(param);
        addLog("新增用户:" + param.getAccount(), param.toString(), "/sys/user/save");
        return new ResponseData<Integer>(HttpStatus.OK.value(), "新增成功", sysUser.getId());
    }

    /*
     * 修改用户
     */
    @PostMapping("/update")
    public ResponseData<SysUser> updateUser(@RequestBody UserAdminParam param) {
        sysUserService.update(param);
        addLog("修改用户:" + param.getAccount(), param.toString(), "/sys/user/update");
        return new ResponseData<SysUser>(HttpStatus.OK.value(), "修改成功", null);
    }

    /*
     * 获取用户列表
     */
    @JsonView(UserDto.UserDtoSimpleView.class)
    @PostMapping("/page")
    public ResponseData<PageInfo<UserDto>> list(
            @PageableDefault(page = 1, size = 10, sort = "operate_time", direction = Direction.DESC) Pageable pageable,
            UserDto dto) {
        return new ResponseData<PageInfo<UserDto>>(HttpStatus.OK.value(), "查询成功", sysUserService.list(pageable, dto));
    }

    /*
     * 获取当前用户权限树
     */
    @GetMapping("/acls")
    public ResponseData<Map<String, Object>> acls() {
        Map<String, Object> map = Maps.newHashMap();
        Integer currentUserId = AuthHolder.getCurrentUserId();
        // map.put("acls", sysTreeService.userAclTree(currentUserId));
        map.put("acls", sysTreeService.userAclModuleTree(currentUserId));
        map.put("roles", sysRoleService.getRoleListByUserId(currentUserId));
        return new ResponseData<Map<String, Object>>(HttpStatus.OK.value(), "查询成功", map);
    }

    /*
     * 取出当前用户所有权限点 注意状态
     */
    @PostMapping("/acl")
    public ResponseData<List<SysAcl>> acl() {
        Integer currentUserId = AuthHolder.getCurrentUserId();
        List<SysAcl> newAclList = Lists.newArrayList();
        List<SysAcl> aclList = sysCoreService.getCurrentUserAclList(currentUserId);
        for (SysAcl sysAcl : aclList) {
            if (sysAcl.getStatus() == 1) {
                newAclList.add(sysAcl);
            }
        }
        return new ResponseData<List<SysAcl>>(HttpStatus.OK.value(), "查询成功", newAclList);
    }

    /*
     * 根据用户ID更新角色
     */
    @PostMapping("/changeRoles")
    public ResponseData<SysUser> changeRoles(@RequestParam("userId") Integer userId,
                                             @RequestParam(value = "roleIds", required = false, defaultValue = "") String roleIds) {
        List<Integer> roleIdList = StringUtil.splitToListInt(roleIds);
        sysRoleUserService.changeUserRoles(userId, roleIdList);
        SysUser user = sysUserService.selectByPrimaryKey(userId);
        addLog("根据用户ID更新角色-用户为:" + user.getAccount(), String.valueOf(userId) + roleIds, "/sys/user/changeRoles");
        return new ResponseData<SysUser>(HttpStatus.OK.value(), "修改成功", null);
    }

    /*
     * 根据用户id获取已选中和未选中的角色
     */
    @PostMapping("/roles")
    public ResponseData<Map<String, List<SysRole>>> roles(@RequestParam("userId") Integer userId) {
        List<SysRole> selectedRoleList = sysRoleUserService.getListByUserId(userId);
        // 前端需求要所有的
        List<SysRole> allRoleList = sysRoleService.getAll();
        // List<SysRole> unSelectedRoleList = Lists.newArrayList();
        // Set<Integer> selectedRoleSet = selectedRoleList.stream().map(sysRole
        // -> sysRole.getId())
        // .collect(Collectors.toSet());
        // for(SysRole sysRole : allRoleList){
        // if(sysRole.getStatus() == 1 &&
        // !selectedRoleSet.contains(sysRole.getId())){
        // unSelectedRoleList.add(sysRole);
        // }
        // }

        Map<String, List<SysRole>> map = Maps.newHashMap();
        map.put("selected", selectedRoleList);
        map.put("allSelected", allRoleList);
        return new ResponseData<Map<String, List<SysRole>>>(HttpStatus.OK.value(), "查询成功", map);

    }

    /*
     * 删除用户
     */

    @PostMapping("/delete")
    public ResponseData<SysUser> delete(@RequestParam("id") int id) {
        SysUser user = sysUserService.selectByPrimaryKey(id);
        sysUserService.delete(id);
        addLog("删除用户:" + user.getAccount(), String.valueOf(id), "/sys/user/delete");
        return new ResponseData<SysUser>(HttpStatus.OK.value(), "删除成功", null);
    }

    /*
     * 用户详情
     */
    @JsonView(UserDto.UserDtoSimpleView.class)
    @PostMapping("/info")
    public ResponseData<UserDto> info(@RequestParam("userId") int userId) {
        UserDto userDto = sysUserService.info(userId);
        return new ResponseData<UserDto>(HttpStatus.OK.value(), "查询成功", userDto);
    }

    /*
     * 编辑用户的时候需要回显角色ID
     */
    @JsonView(UserDto.UserDtoSimpleView.class)
    @PostMapping("/id")
    public ResponseData<UserDto> id(@RequestParam("userId") int userId) {
        UserDto userDto = sysUserService.id(userId);
        return new ResponseData<UserDto>(HttpStatus.OK.value(), "查询成功", userDto);
    }

    /*
     * 批量删除
     */

    @PostMapping("/deletes")
    public ResponseData<SysUser> deletes(@RequestParam("userIds") String userIds) {
        List<Integer> userIdList = StringUtil.splitToListInt(userIds);
        sysUserService.batchDelete(userIdList);
        addLog("批量删除用户ID为:" + userIds + "的用户", userIds, "/sys/user/deletes");
        return new ResponseData<SysUser>(HttpStatus.OK.value(), "删除成功", null);
    }

    /*
     * 导出 excel
     */
    @PostMapping("/export")
    public void export(@RequestParam(value = "deptId", defaultValue = "") Integer deptId,
                       @PageableDefault(page = 1, size = 10, sort = "id,asc") Pageable pageable, HttpServletRequest request,
                       HttpServletResponse response) throws Exception {
        String header = request.getHeader("User-Agent");
        String fileName = "用户" + "." + "xls";
        if (header.contains("Firefox")) {// 判断当前用户使用的是什么浏览（是不是火狐）
            // 是，则使用base64
            fileName = CodeUtils.base64EncodeFileName(fileName);
        } else {
            // 否，则使用URLEncode
            fileName = URLEncoder.encode(fileName, "UTF-8");
        }
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        sysUserService.export(response.getOutputStream(), deptId, pageable);
    }

    /*
     * 批量导入校验
     */
    @PostMapping("/doImport")
    public ResponseData<Object> doImport(MultipartFile file) throws Exception {

        String contentType = file.getContentType();
        if (!contentType.equals("application/vnd.ms-excel")) {
            logger.warn("上传文件不是excel");
            return new ResponseData<Object>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "上传文件不是excel", null);
        }
        InputStream input = file.getInputStream();
        Object obj = sysUserService.doImport(input);
        addLog("批量校验用户", file.getOriginalFilename(), "/sys/user/doImport");
        return new ResponseData<Object>(HttpStatus.OK.value(), "", obj);

    }

    /*
     * 批量导入
     */
    @PostMapping("/batchImport")
    public ResponseData<ImportParam[]> batchImport(@RequestBody ImportParam[] params) {

        sysUserService.batchImport(params);
        addLog("批量导入用户", params.toString(), "/sys/user/batchImport");
        return new ResponseData<ImportParam[]>(HttpStatus.OK.value(), "导入成功", null);
    }

    /*
     * 管理员重置用户密码
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @PostMapping("/resetPwd")
    public ResponseData resetPwd(@RequestBody UserAdminPsParam param) {
        SysUser user = sysUserService.selectByPrimaryKey(param.getId());
        sysUserService.resetPwd(param);
        addLog("管理员重置" + user.getAccount() + "的密码", "", "/sys/user/resetPwd");
        return new ResponseData(HttpStatus.OK.value(), "密码重置成功", null);
    }

    /*
     * 用户重置用户密码
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @PostMapping("/updatePwd")
    public ResponseData updatePwd(@RequestBody UserPsParam param) {
        sysUserService.updatePwd(param, AuthHolder.getCurrentUserId());
        addLog("用户" + AuthHolder.getCurrentUsername() + "重置密码", "", "/sys/user/updatePwd");
        return new ResponseData(HttpStatus.OK.value(), "密码修改成功", null);
    }

    /*
     * 未登录用户重置用户密码
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @PostMapping("/updateCredentials")
    public ResponseData updateCredentials(@RequestBody UserNoLoPsParam param, HttpServletRequest request) {
        sysUserService.updateCredentials(param, request);
        addLog("用户" + param.getKeyword() + "重置密码", "", "/sys/user/updateCredentials");
        return new ResponseData(HttpStatus.OK.value(), "密码修改成功", null);
    }

    // 个人中心
    @GetMapping("/center")
    public ResponseData<Map<String, Object>> center() {

        Map<String, Object> map = sysUserService.center();

        return new ResponseData<Map<String, Object>>(HttpStatus.OK.value(), "查询成功", map);
    }

    // 个人中心修改用户信息
    @PostMapping("/updateCenter")
    public ResponseData<SysUser> updateCenter(UserCenterParam param) {
        sysUserService.updateCenter(param);
        addLog("用户" + AuthHolder.getCurrentUsername() + "修改个人中心", param.toString(), "/sys/user/updateCenter");
        return new ResponseData<SysUser>(HttpStatus.OK.value(), "修改成功", null);
    }

    private void addLog(String remark, String requestObject, String requestUrl) {
        sysLogService.save(SysLog.builder().remark(remark).userId(AuthHolder.getCurrentUserId().longValue())
                .projectName("cyberzone-authorize").requestObject(requestObject).requestUrl(requestUrl)
                .createTime(new Date()).userName(AuthHolder.getCurrentUsername()).ip(IpUtil.getRemoteIp(request))
                .logStatus("操作日志").build());
    }

}
