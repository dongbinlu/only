package com.only.test.Design.iterator;

/**
 * 定义迭代器角色
 */
public interface Iterator {

    boolean hasNext();

    Object next();

    Object remove();

}
