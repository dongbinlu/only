package com.safecode.es.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.safecode.es.entity.ReadBookPd;
import com.safecode.es.mapper.ReadBookPdMapper;
import com.safecode.es.service.ReadBookPdService;

@Service("readBookPdService")
public class ReadBookPdServiceImpl implements ReadBookPdService {

    @Autowired
    private ReadBookPdMapper readBookPdMapper;

    @Override
    public int getBookCount() {
        return readBookPdMapper.getBookCount();
    }

    @Override
    public List<ReadBookPd> getPageList(int page, int size) {
        int start = (page - 1) * size;
        return readBookPdMapper.getPageList(start, size);
    }

}
