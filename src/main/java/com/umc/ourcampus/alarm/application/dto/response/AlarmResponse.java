package com.umc.ourcampus.alarm.application.dto.response;

import com.umc.ourcampus.alarm.domain.Alarm;
import java.time.LocalDateTime;

public record AlarmResponse(
        long id,
        String title,
        String message,
        LocalDateTime scheduledTime,
        boolean checked,
        String type
) {
    public static AlarmResponse from(Alarm alarm) {
        return new AlarmResponse(
                alarm.getId(),
                alarm.getTitle(),
                alarm.getMessage(),
                alarm.getScheduledTime(),
                alarm.isChecked(),
                alarm.getType()
        );
    }
}
