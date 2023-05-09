package com.only.test.jpt;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class MyAspect {

    @Around(value = "@annotation(annotation)")
    public Object myAdvice(ProceedingJoinPoint pjp, MyAnnotation annotation) throws Throwable {
        String[] expressions = annotation.expressions();
        Object[] args = pjp.getArgs();

        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setRootObject(args);

        for (int i = 0; i < expressions.length; i++) {
            String expression = expressions[i];
            ExpressionParser parser = new SpelExpressionParser();
            Expression exp = parser.parseExpression(expression);
            Object result = exp.getValue(context);
            System.out.println("Parameter value: " + result);
        }

        return pjp.proceed(args);
    }
}