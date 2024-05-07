package com.learning.springsecurity.common.validator.FieldNotEmpty;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Objects;

public class FieldNotEmptyValidator implements ConstraintValidator<FieldNotEmptyConstraint,Object> {

    @Override
    public void initialize(FieldNotEmptyConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
        if (Objects.isNull(value)) return true;

        return !value.toString().trim().isEmpty();

    }
}
