package com.example.school.global.validation.annotation;

import com.example.school.global.validation.validator.PageCheckValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = PageCheckValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckPage {
    String message() default "잘못된 페이지입니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}