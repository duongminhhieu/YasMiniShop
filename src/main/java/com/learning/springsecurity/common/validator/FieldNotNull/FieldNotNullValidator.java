package com.learning.springsecurity.common.validator.FieldNotNull;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FieldNotNullValidator implements ConstraintValidator<FieldNotNullConstraint, Object> {

    @Override
    public void initialize(FieldNotNullConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
        return value != null;
    }
}
