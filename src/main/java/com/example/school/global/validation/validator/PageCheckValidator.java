package com.example.school.global.validation.validator;


import com.example.school.global.apiPayload.GeneralException;
import com.example.school.global.apiPayload.status.ErrorStatus;
import com.example.school.global.validation.annotation.CheckPage;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PageCheckValidator implements ConstraintValidator<CheckPage, Integer> {

    @Override
    public void initialize(CheckPage constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (value < 1) {
            context.disableDefaultConstraintViolation();
            throw new GeneralException(ErrorStatus.PAGE_LT_ONE);
        }
        return true;
    }
}
