package com.safecode.cyberzone.authorize.dto;

import java.util.List;

import org.springframework.beans.BeanUtils;

import com.google.common.collect.Lists;
import com.safecode.cyberzone.authorize.entity.SysDept;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

//Tree返回结构 用来做适配
@Setter
@Getter
@ToString
public class DeptLevelDto extends SysDept {
    // 形成树形结构
    private List<DeptLevelDto> deptList = Lists.newArrayList();

    // 适配器
    public static DeptLevelDto adapt(SysDept dept) {
        DeptLevelDto dto = new DeptLevelDto();
        BeanUtils.copyProperties(dept, dto);
        return dto;
    }

}
