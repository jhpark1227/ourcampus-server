package com.example.school.user.domain;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public enum AlertType {
    @Enumerated(EnumType.STRING)
    THREE_DAYS_BEFORE(3 * 24 * 60),
    ONE_DAY_BEFORE(24*60),
    THIRTY_MINUTES_BEFORE(30),
    TEN_MINUTES_BEFORE(10)
    ;

    private final int minutesBefore;

    AlertType(int minutesBefore) {
        this.minutesBefore = minutesBefore;
    }

    public int getMinutesBefore() {
        return minutesBefore;
    }
}