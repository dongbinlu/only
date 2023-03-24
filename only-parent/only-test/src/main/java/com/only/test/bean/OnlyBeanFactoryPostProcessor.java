package com.only.test.bean;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;

/**
 * 是用来修改Bean定义的
 * Bean工厂的后置处理器，什么时候开始执行呢？？？？
 * 执行时机：所有的Bean定义都加载到容器中之后但Bean还没有实例化
 */
@Component
public class OnlyBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        System.out.println("IOC 容器调用了OnlyBeanFactoryPostProcessor的postProcessBeanFactory方法");
        for (String name : beanFactory.getBeanDefinitionNames()) {
            if ("com.only.test.bean.Dog".equals(name)) {
                BeanDefinition beanDefinition = beanFactory.getBeanDefinition(name);
                // 设置为懒加载后，容器启动并不会实例化Dog对象，而是第一次使用时创建，但是Bean定义已经加载到容器中去了。
                beanDefinition.setLazyInit(true);
            }

        }
    }
}
