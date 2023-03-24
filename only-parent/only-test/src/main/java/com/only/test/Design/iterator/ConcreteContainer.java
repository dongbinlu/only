package com.only.test.Design.iterator;

/**
 * 定义具体容器角色
 */
public class ConcreteContainer implements Container {

    public Object[] items;

    /**
     * 数组长度
     */
    public int size;

    /**
     * 数组下标
     */
    public int index;

    public ConcreteContainer() {
        this.items = new Object[100];
        this.size = 0;
        this.index = 0;

    }

    @Override
    public Iterator iterator() {
        return new ConcreteIterator(this);
    }

    @Override
    public void add(Object obj) {
        items[index++] = obj;
        size++;
    }

    @Override
    public int getSize() {
        return this.size;
    }

    @Override
    public Object get(int index) {
        return items[index];
    }
}
