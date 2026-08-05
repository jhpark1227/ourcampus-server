package com.umc.ourcampus.alarm.application.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record AlarmSliceResponse(
        List<AlarmResponse> alarms,
        LocalDateTime nextCursorScheduledTime,
        Long nextCursorId,
        boolean hasNext
) {
    public static AlarmSliceResponse of(List<AlarmResponse> alarms, boolean hasNext) {
        if (alarms.isEmpty()) {
            return new AlarmSliceResponse(alarms, null, null, false);
        }
        AlarmResponse last = alarms.get(alarms.size() - 1);
        return new AlarmSliceResponse(alarms, last.scheduledTime(), last.id(), hasNext);
    }
}