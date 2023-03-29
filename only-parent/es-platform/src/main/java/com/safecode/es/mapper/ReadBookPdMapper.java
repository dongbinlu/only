package com.safecode.es.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.safecode.es.entity.ReadBookPd;

public interface ReadBookPdMapper {

    public List<ReadBookPd> getPageList(@Param("start") Integer start, @Param("size") Integer size);

    List<Map<String, Object>> buildESQuery(@Param("id") Integer id);

    int getBookCount();
}