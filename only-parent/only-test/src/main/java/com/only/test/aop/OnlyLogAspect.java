package com.only.test.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;

import java.util.Arrays;

@Aspect
public class OnlyLogAspect {

    @Pointcut("execution(* com.only.test.aop.OnlyCalculate.*(..))")
    public void pointCut() {
    }

    @Before(value = "pointCut()")
    public void methodBefore(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        System.out.println("执行目标方法【" + methodName + "】之前执行<前置通知>,入参" + Arrays.asList(joinPoint.getArgs()));
    }

    @After(value = "pointCut()")
    public void methodAfter(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        System.out.println("执行目标方法【" + methodName + "】之前执行<后置通知>,入参" + Arrays.asList(joinPoint.getArgs()));
       }

    @AfterReturning(value = "pointCut()")
    public void methodReturning(JoinPoint joinPoint) {
                                       String methodName = joinPoint.getSignature().getName();
        System.out.println("执行目标方法【" + methodName + "】之前执行<返回通知>,入参" + Arrays.asList(joinPoint.getArgs()));
    }

    @AfterThrowing(value = "pointCut()")
    public void methodAfterThrowing(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        System.out.println("执行目标方法【" + methodName + "】之前执行<异常通知>,入参" + Arrays.asList(joinPoint.getArgs()));
    }
}