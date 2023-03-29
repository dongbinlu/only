package com.safecode.cyberzone.authorize.dto;

import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonView;
import com.safecode.cyberzone.authorize.entity.SysUser;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class UserDto extends SysUser {

    public interface UserDtoSimpleView extends SysUserSimpleView {
    }

    ;

    public interface UserDtoDetailView extends SysUserDetailView {
    }

    ;

    // 是否要默认选中
    private boolean checked = false;

    private Integer fixRoleId;
    @JsonView(UserDtoSimpleView.class)
    private Integer roleId;
    @JsonView(UserDtoSimpleView.class)
    private List<Integer> roleIdList;

    @JsonView(UserDtoSimpleView.class)
    private String roleName;

    @JsonView(UserDtoSimpleView.class)
    private String platformName;

    @JsonView(UserDtoSimpleView.class)
    private Date beginDate;

    @JsonView(UserDtoSimpleView.class)
    private Date endDate;

    public static UserDto adapt(SysUser sysUser) {
        UserDto dto = new UserDto();
        BeanUtils.copyProperties(sysUser, dto);
        return dto;
    }

}
