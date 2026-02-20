package com.umc.ourcampus.reservation.presentation.dto;

import com.umc.ourcampus.reservation.domain.AlarmTiming;

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
