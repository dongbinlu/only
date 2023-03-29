package com.safecode.security.subject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.safecode.security.subject.entity.SysUser;
import com.safecode.security.subject.service.SysUserService;

@RestController
@RequestMapping("/sys/user")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    @GetMapping("/{id}")
    public SysUser id(@PathVariable("id") Integer id) {
        SysUser sysUser = sysUserService.selectByPrimaryKey(id);
        return sysUser;
    }

    @PostMapping("/page")
    public PageInfo<SysUser> page(@RequestParam(value = "deptId", defaultValue = "") Integer deptId,
                                  @PageableDefault(page = 1, size = 5, sort = "id,asc") Pageable pageable) {
        PageInfo<SysUser> pageInfo = sysUserService.getPageByDeptId(deptId, pageable);
        return pageInfo;
    }

}
