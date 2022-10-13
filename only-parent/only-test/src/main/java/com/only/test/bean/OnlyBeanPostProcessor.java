package com.only.test.bean;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * 用来修改Bean对象的属性的
 */
@Component
public class OnlyBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof Person) {
            System.out.println("BeanPostProcessor的postProcessBeforeInitialization方法:" + beanName);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof Person) {
            System.out.println("BeanPostProcessor的postProcessAfterInitialization方法:" + beanName);
        }
        return bean;
    }
}
