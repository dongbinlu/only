package com.only.mvc.servletInitializer;

import javax.servlet.*;
import javax.servlet.annotation.HandlesTypes;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Set;

/**
 * 类的描述
 * <p>
 * 这个特性是servlet3.0规范，8.24小节：Shared libraries / runtimes pluggability
 * 规则1：
 * 在我们的servlet容器启动的时候，他会读取我们classpath下的jar包的所有ServletContainerInitializer
 * 的实现类的onStartup方法，但是ServletContainerInitializer的实现类必须要绑定在META-INF/services目录
 * 下，文件名称为：javax.servlet.ServletContainerInitializer
 * 内容：ServletContainerInitializer实现类的全类名路径
 * 规则2：
 * 我们通过HandlesType来指定传入的类型到onStartup方法。
 */

//此注解可以将IOnly接口的所有实现类包含抽象类都放入Set集合中
@HandlesTypes(value = IOnly.class)
public class OnlyServletContainerInitializer implements ServletContainerInitializer {
    @Override
    public void onStartup(Set<Class<?>> set, ServletContext ctx) throws ServletException {


        ArrayList<IOnly> list = new ArrayList<>();

        if (set != null) {
            for (Class iOnlyClass : set) {
                // 如果不是接口，不是抽象类，是IOnly接口的实现类
                if (!iOnlyClass.isInterface() && !Modifier.isAbstract(iOnlyClass.getModifiers()) && IOnly.class.isAssignableFrom(iOnlyClass)) {
                    try {
                        list.add((IOnly) iOnlyClass.newInstance());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        for (IOnly iOnly : list) {
            iOnly.sayHello();
        }

        // 通过ServletContext来注册我们的servlet listner
        ctx.addListener(AngleListener.class);

        //注册servlet
        ServletRegistration.Dynamic servletRegistration = ctx.addServlet("angleServlet", new AngleServlet());
        servletRegistration.addMapping("/angle");

        //注册filter
        FilterRegistration.Dynamic filterRegistration = ctx.addFilter("angleFilter", AngleFilter.class);
        filterRegistration.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*");


    }
}
