package com.safecode.security.permission.dto;

import org.springframework.beans.BeanUtils;

import com.safecode.security.permission.entity.SysAcl;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AclDto extends SysAcl {

    // 是否要默认选中
    private boolean checked = false;

    // 是否有权限操作
    private boolean hasAcl = false;

    public static AclDto adatp(SysAcl acl) {
        AclDto dto = new AclDto();
        BeanUtils.copyProperties(acl, dto);
        return dto;
    }

}
