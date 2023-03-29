package com.safecode.es.service;

public interface ReIndexService {

    /**
     * 对book索引进行全量构建
     *
     * @return
     */
    boolean reIndexBooks();

}
