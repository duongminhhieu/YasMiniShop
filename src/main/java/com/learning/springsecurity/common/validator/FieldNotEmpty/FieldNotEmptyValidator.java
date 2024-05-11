package com.learning.springsecurity.common.validator.FieldNotEmpty;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Collection;
import java.util.Objects;

public class FieldNotEmptyValidator implements ConstraintValidator<FieldNotEmptyConstraint, Object> {

    @Override
    public void initialize(FieldNotEmptyConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
        if (Objects.isNull(value)) return true;

        return switch (value) {
            case String s -> !s.trim().isEmpty();
            case Collection<?> collection -> !collection.isEmpty();
            case Double v -> !v.isNaN() && !v.isInfinite();
            case Float v -> !v.isNaN() && !v.isInfinite();
            default -> true;
        };
    }
}
