package com.safecode.es.service;

import java.util.List;

import com.safecode.es.entity.ReadBookPd;

public interface ReadBookPdService {

    public int getBookCount();

    public List<ReadBookPd> getPageList(int page, int size);

}
