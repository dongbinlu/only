package com.only.test.Design.iterator;

/**
 * 定义具体迭代器角色
 */
public class ConcreteIterator implements Iterator {

    /**
     * 指定容器中末尾元素下一个位置
     */
    private int current = 0;

    private ConcreteContainer concreteContainer;

    public ConcreteIterator(ConcreteContainer concreteContainer) {
        this.concreteContainer = concreteContainer;
    }

    /**
     * 是否含有下一个元素
     * true表示有，false表示没有
     * 比较当前位置和容器的容量
     *
     * @return
     */
    @Override
    public boolean hasNext() {
        return current < concreteContainer.size;
    }

    /**
     * 返回当前位置，后移动当前位置
     *
     * @return
     */
    @Override
    public Object next() {
        return concreteContainer.items[current++];
    }

    @Override
    public Object remove() {
        return null;
    }
}
