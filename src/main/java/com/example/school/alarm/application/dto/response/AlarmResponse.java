package com.example.school.alarm.application.dto.response;

import com.example.school.alarm.domain.Alarm;
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
        System.out.println(alarm.isChecked());
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
