package com.example.school.global.validation.validator;


import com.example.school.global.apiPayload.GeneralException;
import com.example.school.global.apiPayload.status.ErrorStatus;
import com.example.school.global.validation.annotation.CheckAnnouncementType;
import com.example.school.notice.domain.NoticeType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.EnumUtils;

public class AnnouncementTypeCheckValidator implements ConstraintValidator<CheckAnnouncementType, String> {

    @Override
    public void initialize(CheckAnnouncementType constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value != null) {
            if (value.trim().equals("") || !EnumUtils.isValidEnum(NoticeType.class, value)) {
                context.disableDefaultConstraintViolation();
                throw new GeneralException(ErrorStatus.BAD_QUERY_STRING);
            }
        }
        return true;
    }
}
