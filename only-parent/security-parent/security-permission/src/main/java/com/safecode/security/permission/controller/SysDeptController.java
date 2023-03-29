package com.safecode.security.permission.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.safecode.security.permission.common.JsonData;
import com.safecode.security.permission.param.DeptParam;
import com.safecode.security.permission.service.SysDeptService;
import com.safecode.security.permission.service.impl.SysTreeService;

@RestController
@RequestMapping("/sys/dept")
public class SysDeptController {

    @Autowired
    private SysDeptService sysDeptService;

    @Autowired
    private SysTreeService sysTreeService;

    /**
     * 新增部门
     *
     * @param param
     * @return
     */
    @PostMapping("/save")
    public JsonData saveDept(DeptParam param) {
        sysDeptService.save(param);
        return JsonData.success();
    }

    /**
     * 部门树
     *
     * @return
     */
    @GetMapping("/tree")
    public JsonData tree() {
        return JsonData.success(sysTreeService.deptTree());
    }

    /**
     * 修改部门 注：子级部门修改有bug
     *
     * @param param
     * @return
     */
    @PostMapping("/update")
    public JsonData updateDept(DeptParam param) {
        sysDeptService.update(param);
        return JsonData.success();
    }

    /**
     * 删除部门
     *
     * @param id
     * @return
     */
    @PostMapping("/delete")
    public JsonData deleteDept(@RequestParam("id") int id) {
        sysDeptService.delete(id);
        return JsonData.success();
    }

}
