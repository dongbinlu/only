package com.safecode.cyberzone.auth.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.safecode.cyberzone.auth.entity.SysLog;
import com.safecode.cyberzone.auth.mapper.SysLogMapper;
import com.safecode.cyberzone.auth.service.SysLogService;

@Service("sysLogService")
public class SysLogServiceImpl implements SysLogService {

    @Autowired
    private SysLogMapper sysLogMapper;

    @Override
    public void save(SysLog sysLog) {
        sysLogMapper.insertSelective(sysLog);
    }

}
