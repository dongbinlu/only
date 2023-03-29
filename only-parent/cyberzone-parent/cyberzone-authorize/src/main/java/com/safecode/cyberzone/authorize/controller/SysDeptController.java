package com.safecode.cyberzone.authorize.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.safecode.cyberzone.authorize.dto.DeptLevelDto;
import com.safecode.cyberzone.authorize.entity.SysDept;
import com.safecode.cyberzone.authorize.mapper.SysDeptMapper;
import com.safecode.cyberzone.authorize.param.DeptParam;
import com.safecode.cyberzone.authorize.service.SysDeptService;
import com.safecode.cyberzone.authorize.service.impl.SysTreeService;
import com.safecode.cyberzone.base.dto.ResponseData;

@RestController
@RequestMapping("/sys/dept")
public class SysDeptController {
    @Autowired
    private SysDeptService sysDeptService;

    @Autowired
    private SysTreeService sysTreeService;

    @Autowired
    private SysDeptMapper sysDeptMapper;

    /*
     * 新增部门
     */

    @PostMapping("/save")
    public ResponseData<SysDept> saveDept(@RequestBody DeptParam param) {
        sysDeptService.save(param);
        return new ResponseData<SysDept>(HttpStatus.OK.value(), "新增成功", null);
    }

    /*
     * 修改部门
     */

    @PostMapping("/update")
    public ResponseData<SysDept> updateDept(@RequestBody DeptParam param) {
        sysDeptService.update(param);
        return new ResponseData<SysDept>(HttpStatus.OK.value(), "修改成功", null);
    }

    /*
     * 部门树
     */

    @GetMapping("/tree")
    public ResponseData<List<DeptLevelDto>> tree() {
        List<DeptLevelDto> dtoList = sysTreeService.deptTree();
        return new ResponseData<List<DeptLevelDto>>(HttpStatus.OK.value(), "查询成功", dtoList);
    }

    /*
     * 删除部门
     */

    @PostMapping("/delete")
    public ResponseData<SysDept> delete(@RequestParam("id") int id) {
        sysDeptService.delete(id);
        return new ResponseData<SysDept>(HttpStatus.OK.value(), "删除成功", null);
    }

}
