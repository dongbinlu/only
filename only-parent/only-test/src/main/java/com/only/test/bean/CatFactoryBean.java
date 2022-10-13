package com.only.test.bean;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

@Component
public class CatFactoryBean implements FactoryBean<Cat> {
    //返回bean的对象
    @Override
    public Cat getObject() throws Exception {
        return new Cat();
    }

    //返回bean的类型
    @Override
    public Class<?> getObjectType() {
        return Cat.class;
    }

    //是否为单利
    @Override
    public boolean isSingleton() {
        return true;
    }
}