package com.only.test.prepareRefresh;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * 系统启动的时候必须有only环境变量，否则就抛异常
 */
public class OnlyApplicationContext extends AnnotationConfigApplicationContext {

    @Override
    protected void initPropertySources() {
        super.initPropertySources();

        getEnvironment().setRequiredProperties("only");
    }

    public OnlyApplicationContext(Class<?>... annotatedClasses) {
        super(annotatedClasses);
    }
}
