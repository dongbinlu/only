package com.only.test.proxy.high;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.ConstructorArgumentValues;

/**
 * 执行时机：bean定义已经加载到容器但还没实例化
 */
public class OnlyBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

        BeanDefinition beanDefinition = beanFactory.getBeanDefinition("userInfoMapper");

        String sourceClassName = beanDefinition.getBeanClassName();

        //beanDefinition.setBeanName(OnlyMapperFactoryBean.class);

        ConstructorArgumentValues constructorArgumentValues = new ConstructorArgumentValues();
        constructorArgumentValues.addGenericArgumentValue(sourceClassName);

        //beanDefinition.s

    }
}
