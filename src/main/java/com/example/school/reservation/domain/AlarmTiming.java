package com.example.school.reservation.domain;

import java.time.Duration;
import lombok.Getter;

@Getter
public enum AlarmTiming {

    THREE_DAYS_BEFORE(Duration.ofDays(3), "3일 전"),
    ONE_DAY_BEFORE(Duration.ofDays(1), "1일 전"),
    THIRTY_MINUTES_BEFORE(Duration.ofHours(1), "1시간 전"),
    TEN_MINUTES_BEFORE(Duration.ofMinutes(10), "10분 전");

    private final Duration duration;
    private final String displayName;

    AlarmTiming(Duration duration, String displayName) {
        this.duration = duration;
        this.displayName = displayName;
    }
}