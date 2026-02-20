package com.umc.ourcampus.review.domain;

import com.umc.ourcampus.global.apiPayload.status.ErrorStatus;
import com.umc.ourcampus.global.exception.ApplicationException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record StarRating(
        @Column(name = "start_rating")
        int value
) {
    public StarRating {
        if (1 > value || 5 < value) {
            throw new ApplicationException(ErrorStatus.STAR_RATING_RANGE);
        }
    }
}
