package com.only.test.Design.iterator;

/**
 * 定义容器角色
 */
public interface Container {

    /**
     * 迭代器
     *
     * @return
     */
    Iterator iterator();

    /**
     * 增加元素
     *
     * @param obj
     */
    void add(Object obj);

    /**
     * 获取大小
     *
     * @return
     */
    int getSize();

    /**
     * 获取元素
     *
     * @param index
     * @return
     */
    Object get(int index);


}
