package com.example.school.review.domain;

import com.example.school.global.apiPayload.status.ErrorStatus;
import com.example.school.global.exception.ApplicationException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record StarRating(
        @Column(name = "start_rating")
        int value
) {
    public StarRating {
        if (1 > value || 5 < value) {
            throw new ApplicationException(ErrorStatus.EMAIL_CODE_ERROR);
        }
    }
}
