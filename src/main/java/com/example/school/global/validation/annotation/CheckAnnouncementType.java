package com.example.school.global.validation.annotation;

import com.example.school.global.validation.validator.AnnouncementTypeCheckValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = AnnouncementTypeCheckValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckAnnouncementType {
    String message() default "잘못된 쿼리 스트링입니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}