package com.safecode.cyberzone.authorize.dto;

import org.springframework.beans.BeanUtils;

import com.safecode.cyberzone.authorize.entity.SysAcl;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class AclDto extends SysAcl {

    // 是否要默认选中
    private boolean checked = false;

    // 是否有权限操作    他能操作的权限不能超出已分配权限的上限
    private boolean hasAcl = false;

    public static AclDto adapt(SysAcl acl) {
        AclDto dto = new AclDto();
        BeanUtils.copyProperties(acl, dto);
        return dto;
    }

}
