package com.safecode.cyberzone.authorize.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.github.pagehelper.Page;
import com.safecode.cyberzone.authorize.dto.UserDto;
import com.safecode.cyberzone.authorize.entity.SysUser;
import com.safecode.cyberzone.base.log.SysLog;

public interface SysUserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysUser record);

    int insertSelective(SysUser record);

    SysUser selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysUser record);

    int updateByPrimaryKey(SysUser record);

    int countByMail(@Param("mail") String mail, @Param("id") Integer id);

    int countByTelephone(@Param("telephone") String telephone, @Param("id") Integer id);

    int countByAccount(@Param("account") String account, @Param("id") Integer id);

    Page<SysUser> getPageByDeptId(@Param("deptId") Integer deptId);

    List<SysUser> getByIdList(@Param("idList") List<Integer> idList);

    Page<UserDto> getByIdList2(@Param("idList") List<Integer> idList, @Param("username") String username,
                               @Param("account") String account);

    List<SysUser> getAll();

    int countByDeptId(@Param("deptId") int deptId);

    void batchDelete(@Param("userIdList") List<Integer> userIdList);

    SysUser findByKeyword(@Param("keyword") String keyword);

    Page<UserDto> list(UserDto dto);

    Page<UserDto> newList(@Param("userIdList") List<Integer> userIdList, @Param("account") String account,
                          @Param("username") String username, @Param("platformName") String platformName,
                          @Param("roleId") Integer roleId);

    Page<UserDto> new2List(@Param("account") String account, @Param("username") String username,
                           @Param("platformName") String platformName, @Param("roleId") Integer roleId);

    Page<SysUser> roleToUser(@Param("userIdList") List<Integer> userIdList, @Param("account") String account,
                             @Param("username") String username);

    public SysLog getLoginInfo(@Param("username") String username);

    public List<SysLog> getLoginInfo2(@Param("username") String username);

}