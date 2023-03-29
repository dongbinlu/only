package com.safecode.cyberzone.authorize.service;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Pageable;

import com.github.pagehelper.PageInfo;
import com.safecode.cyberzone.authorize.dto.UserDto;
import com.safecode.cyberzone.authorize.entity.SysUser;
import com.safecode.cyberzone.authorize.param.ImportParam;
import com.safecode.cyberzone.authorize.param.UserAdminParam;
import com.safecode.cyberzone.authorize.param.UserAdminPsParam;
import com.safecode.cyberzone.authorize.param.UserCenterParam;
import com.safecode.cyberzone.authorize.param.UserNoLoPsParam;
import com.safecode.cyberzone.authorize.param.UserParam;
import com.safecode.cyberzone.authorize.param.UserPsParam;

public interface SysUserService {

    public SysUser save(UserParam param);

    public Object imports(List<ImportParam> listParam);

    public void update(UserAdminParam param);

    public void updateCenter(UserCenterParam param);

    public PageInfo<UserDto> list(Pageable pageable, UserDto dto);

    public UserDto info(Integer id);

    public List<SysUser> getAll();

    public UserDto id(Integer id);

    public void delete(int userId);

    public SysUser selectByPrimaryKey(int userId);

    public void batchDelete(List<Integer> userIdList);

    public void export(OutputStream out, Integer deptId, Pageable pageable) throws Exception;

    public Object doImport(InputStream input) throws Exception;

    public void resetPwd(UserAdminPsParam param);

    public void updatePwd(UserPsParam param, int currentUserId);

    public void updateCredentials(UserNoLoPsParam param, HttpServletRequest request);

    public Map<String, Object> center();

    ImportParam[] batchImport(ImportParam[] params);

}
