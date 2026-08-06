package com.umc.ourcampus.facility.domain;

import com.umc.ourcampus.global.exception.ApplicationException;
import com.umc.ourcampus.global.exception.ErrorStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record SearchKeyword(
        @Column(name = "keyword") String value
) {

    private static final int MAX_LENGTH = 50;

    public SearchKeyword {
        if (value == null || value.isBlank()) {
            throw new ApplicationException(ErrorStatus.SEARCH_CONDITION_ERROR);
        }
        value = value.trim().replaceAll("\\s+", " ");
        if (value.length() > MAX_LENGTH) {
            throw new ApplicationException(ErrorStatus.SEARCH_CONDITION_ERROR);
        }
    }
}