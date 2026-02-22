package com.umc.ourcampus.reservation.domain;

import com.umc.ourcampus.global.exception.ErrorStatus;
import com.umc.ourcampus.global.exception.ApplicationException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record HeadCount(
        @Column(name = "head_count") int value
) {
    public HeadCount {
        if (value < 1 || 1000 < value) {
            throw new ApplicationException(ErrorStatus.HEAD_COUNT_INVALID_RANGE);
        }
    }
}
