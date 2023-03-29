package com.safecode.security.subject.aspect;

import java.util.Date;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
// 放在Spring容器中 ， 让其生效
@Component("timeAspect")
public class TimeAspect {

    private Logger logger = LoggerFactory.getLogger(getClass());
    // 在方法调用之前
    // @Before

    // 在方法返回成功之后
    // @After

    // 在方法抛出异常时
    // @AfterThorwing

    // 完全覆盖前三个注解 一般只用这一个注解搞定 此注解定义了1，在哪些方法上起作用 2，在什么时候起作用
    // 执行UserController中的任何方法 ， 任何参数
    @Around("execution(* com.safecode.security.subject.test.UserController.*(..))")
    public Object handlerControllerMethod(ProceedingJoinPoint pjp) throws Throwable {

        logger.info("TimeAspect 开始");

        long start = new Date().getTime();

        // 获取方法的参数
        Object[] args = pjp.getArgs();
        for (Object arg : args) {
            logger.info("方法参数：" + arg);
        }

        // 此方法相当于Filter的doFilter方法
        Object object = pjp.proceed();

        logger.info("TimeAspect 耗时 ：" + (new Date().getTime() - start));

        logger.info("TimeAspect 结束");
        return object;
    }

}
