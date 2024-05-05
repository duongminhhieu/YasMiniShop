package com.learning.springsecurity.auth.validator.FieldNotNull;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;

@Target({FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FieldNotNullValidator.class)
public @interface FieldNotNullConstraint {
    String message();
    String field() ;
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
