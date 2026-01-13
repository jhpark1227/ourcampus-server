package com.example.school.reservation.application.dto.response;

import com.example.school.reservation.domain.TimeSlot;
import java.time.LocalDateTime;

public record TimeSlotWithBookedResponse(
        LocalDateTime startTime,
        long durationMinutes,
        boolean isBooked
) {
    public static TimeSlotWithBookedResponse of(TimeSlot timeSlot, boolean booked) {
        return new TimeSlotWithBookedResponse(timeSlot.startTime(), timeSlot.getDuration().toMinutes(), booked);
    }
}
