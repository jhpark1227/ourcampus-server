package com.example.school.global.validation.validator;

import com.example.school.global.apiPayload.status.ErrorStatus;
import com.example.school.global.validation.annotation.ExistReview;
import com.example.school.review.application.ReviewService;
import com.example.school.review.domain.Review;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReviewExistValidator implements ConstraintValidator<ExistReview, Long> {

    private final ReviewService reviewService;

    @Override
    public void initialize(ExistReview constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        Optional<Review> target = reviewService.findById(value);

        if (target.isEmpty()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(ErrorStatus.REVIEW_NOT_FOUND.toString())
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}