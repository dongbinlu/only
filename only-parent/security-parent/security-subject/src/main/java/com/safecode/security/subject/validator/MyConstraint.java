/**
 *
 */
package com.safecode.security.subject.validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

/**
 * @author v_boy 自定义注解
 */

// 目标：该注解标注在什么位置，例如类，方法，属性......上
@Target({ElementType.METHOD, ElementType.FIELD})

// 表明此注解是运行时期的注解
@Retention(RetentionPolicy.RUNTIME)

// 表明是自己的校验器 validateBy需要指定类，该注解放在哪个类上
@Constraint(validatedBy = MyConstarinValidator.class)
public @interface MyConstraint {

    String message();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
