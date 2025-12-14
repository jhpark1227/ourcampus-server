package com.example.school.reservation.application.dto.request;

import com.example.school.reservation.domain.AlarmTiming;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Set;
import org.hibernate.validator.constraints.Range;

public record ReservationRequest(
        long facilityId,
        @Range(max = Integer.MAX_VALUE) int headCount,
        @Future LocalDateTime startTime,
        @Range(max = Integer.MAX_VALUE) int durationMinutes,
        @Size Set<AlarmTiming> alarmTimings
) {
}
