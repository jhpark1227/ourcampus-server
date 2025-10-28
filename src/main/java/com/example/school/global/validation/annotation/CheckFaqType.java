package com.example.school.global.validation.annotation;

import com.example.school.global.validation.validator.FaqTypeCheckValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = FaqTypeCheckValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckFaqType {
    String message() default "잘못된 쿼리 스트링입니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}