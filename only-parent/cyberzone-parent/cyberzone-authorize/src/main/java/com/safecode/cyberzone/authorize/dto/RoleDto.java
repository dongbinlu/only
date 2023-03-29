package com.safecode.cyberzone.authorize.dto;

import com.safecode.cyberzone.authorize.entity.SysRole;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class RoleDto extends SysRole {

    private String platformName;

    private int unum;


}
