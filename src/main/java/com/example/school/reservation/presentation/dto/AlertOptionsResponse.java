package com.example.school.reservation.presentation.dto;

import com.example.school.reservation.domain.AlarmTiming;

public record AlertOptionsResponse(
        String name,
        String displayName,
        long minutes
) {
    public static AlertOptionsResponse from(AlarmTiming alarmTiming) {
        return new AlertOptionsResponse(
                alarmTiming.name(),
                alarmTiming.getDisplayName(),
                alarmTiming.getDuration().toMinutes()
        );
    }
}
