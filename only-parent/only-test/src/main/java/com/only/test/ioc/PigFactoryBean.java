package com.only.test.ioc;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

@Component
public class PigFactoryBean implements FactoryBean<Pig> {

    @Override
    public Pig getObject() throws Exception {
        return new Pig();
    }

    @Override
    public Class<?> getObjectType() {
        return Pig.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }


}
