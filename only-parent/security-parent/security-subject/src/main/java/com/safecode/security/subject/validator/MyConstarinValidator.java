package com.safecode.security.subject.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyConstarinValidator implements ConstraintValidator<MyConstraint, Object> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    // 在这里可以注入spring容器中的任何Bean来执行自己的校验逻辑

    @Override
    public void initialize(MyConstraint constraintAnnotation) {
        logger.info("my validator init");
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        logger.info(value.toString());
        return false;
    }

}
