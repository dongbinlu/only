package com.only.mvc.plus.mvc;

import com.only.mvc.plus.anno.MvcMapping;
import lombok.Builder;
import lombok.Data;
import org.springframework.context.ApplicationContext;
import org.springframework.util.Assert;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

public class MvcBeanFactory {

    // api 接口容器
    private HashMap<String, MvcBean> apiMap = new HashMap<>();

    private ApplicationContext applicationContext;

    public MvcBeanFactory(ApplicationContext applicationContext) {
        Assert.notNull(applicationContext, "applicationContext不可以为空");
        this.applicationContext = applicationContext;
        loadApiFromSpringBeans();
    }

    private void loadApiFromSpringBeans() {
        apiMap.clear();

        String[] beanNames = applicationContext.getBeanDefinitionNames();
        for (String beanName : beanNames) {
            Class<?> type = applicationContext.getType(beanName);
            for (Method method : type.getMethods()) {
                // 通过反射获取MvcMapping注解
                MvcMapping mvcMapping = method.getAnnotation(MvcMapping.class);
                if (mvcMapping != null) {
                    // 封装成一个MvcBean
                    addApiItem(mvcMapping, beanName, method);
                }
            }
        }
    }

    private void addApiItem(MvcMapping mvcMapping, String beanName, Method method) {

        MvcBean mvcBean = MvcBean.builder().apiName(mvcMapping.value()).targetName(beanName).targetMethod(method).applicationContext(applicationContext).build();
        apiMap.put(mvcMapping.value(), mvcBean);

    }

    public MvcBean getMvcBean(String apiName){
        return apiMap.get(apiName);
    }

    @Data
    @Builder
    public static class MvcBean {

        // 接口api
        private String apiName;

        // ioc bean名称
        private String targetName;

        // bean实例
        private Object target;

        //  目标方法
        private Method targetMethod;

        private ApplicationContext applicationContext;

        public Object run(Object... args) throws InvocationTargetException, IllegalAccessException {
            // 懒加载
            if (target == null) {
                // spring ioc 容器里面取bean
                target = applicationContext.getBean(targetName);
            }
            return targetMethod.invoke(target, args);
        }
    }

}
