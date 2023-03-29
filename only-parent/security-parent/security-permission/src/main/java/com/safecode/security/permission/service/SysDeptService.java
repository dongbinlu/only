package com.safecode.security.permission.service;

import com.safecode.security.permission.param.DeptParam;

public interface SysDeptService {
    public void save(DeptParam param);

    public void update(DeptParam param);

    public void delete(Integer deptId);
}
