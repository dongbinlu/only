package com.safecode.cyberzone.authorize.service;

import com.safecode.cyberzone.authorize.param.DeptParam;

public interface SysDeptService {

    public void save(DeptParam param);

    public void update(DeptParam param);

    public void delete(int deptId);

}
