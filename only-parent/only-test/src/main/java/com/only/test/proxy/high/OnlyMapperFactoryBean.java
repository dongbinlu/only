package com.only.test.proxy.high;

import org.springframework.beans.factory.FactoryBean;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

public class OnlyMapperFactoryBean implements FactoryBean<UserInfoMaper> {

    private Class targetClass;

    public OnlyMapperFactoryBean(Class targetClass) {
        this.targetClass = targetClass;
    }


    @Override
    public UserInfoMaper getObject() throws Exception {

        InvocationHandler handler = new InvocationHandler() {

            @Override
            public Object invoke(Object proxy, Method method, Object[] args)
                    throws Throwable {

                System.out.println("被调用的方法：" + method.getName() + "入参：" + Arrays.asList(args));
                RemoteClient annotation = method.getAnnotation(RemoteClient.class);
                String value = annotation.value();
                System.out.println("执行的业务sql：" + value);
                return null;

            }

        };
        UserInfoMaper userInfoMaper = (UserInfoMaper) Proxy.newProxyInstance(targetClass.getClassLoader(), new Class[]{targetClass}, handler);

        return userInfoMaper;
    }

    @Override
    public Class<?> getObjectType() {
        return null;
    }
}
