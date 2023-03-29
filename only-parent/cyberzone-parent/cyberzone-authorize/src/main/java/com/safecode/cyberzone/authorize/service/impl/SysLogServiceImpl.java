package com.safecode.cyberzone.authorize.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.safecode.cyberzone.authorize.entity.SysLog;
import com.safecode.cyberzone.authorize.mapper.SysLogMapper;
import com.safecode.cyberzone.authorize.service.SysLogService;

@Service("sysLogService")
public class SysLogServiceImpl implements SysLogService {

    @Autowired
    private SysLogMapper sysLogMapper;

    @Override
    public void save(SysLog sysLog) {
        sysLogMapper.insertSelective(sysLog);
    }

}
