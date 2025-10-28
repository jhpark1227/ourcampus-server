package com.example.school.global.validation.validator;

import com.example.school.facility.application.FacilityQueryService;
import com.example.school.facility.domain.Facility;
import com.example.school.global.apiPayload.status.ErrorStatus;
import com.example.school.global.validation.annotation.ExistFacility;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FacilityExistValidator implements ConstraintValidator<ExistFacility, Long> {

    private final FacilityQueryService facilityQueryService;

    @Override
    public void initialize(ExistFacility constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        Optional<Facility> target = facilityQueryService.findFacility(value);

        if (target.isEmpty()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(ErrorStatus.FACILITY_NOT_FOUND.toString())
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}