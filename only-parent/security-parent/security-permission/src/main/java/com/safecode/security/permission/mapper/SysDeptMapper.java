package com.safecode.security.permission.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.safecode.security.permission.entity.SysDept;

public interface SysDeptMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysDept record);

    int insertSelective(SysDept record);

    SysDept selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysDept record);

    int updateByPrimaryKey(SysDept record);

    int countByNameAndParentId(@Param("parentId") Integer parentId, @Param("name") String name,
                               @Param("id") Integer id);

    List<SysDept> getAll();

    List<SysDept> getChildDeptListByLevel(SysDept dept);

    void batchUpdateLevel(@Param("sysDeptList") List<SysDept> sysDeptList);

    int countByParentId(@Param("deptId") Integer deptId);
}