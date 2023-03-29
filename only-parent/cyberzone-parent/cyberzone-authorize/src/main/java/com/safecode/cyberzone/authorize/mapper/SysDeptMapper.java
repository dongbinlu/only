package com.safecode.cyberzone.authorize.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.safecode.cyberzone.authorize.entity.SysDept;

public interface SysDeptMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysDept record);

    int insertSelective(SysDept record);

    SysDept selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysDept record);

    int updateByPrimaryKey(SysDept record);

    int countByNameAndParentId(@Param("parentId") Integer parentId, @Param("name") String name,
                               @Param("id") Integer id);

    List<SysDept> getChildDeptListByLevel(SysDept dept);

    void batchUpdateLevel(@Param("sysDeptList") List<SysDept> sysDeptList);

    List<SysDept> getAllDept();

    int countByParentId(@Param("id") int id);
}