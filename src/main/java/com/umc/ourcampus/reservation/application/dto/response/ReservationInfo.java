package com.umc.ourcampus.reservation.application.dto.response;

import java.time.Duration;
import java.util.List;

public record ReservationInfo(
        List<TimeSlotWithBookedResponse> timeSlots,
        long reservationLimitMinutes
) {
    public static ReservationInfo of(List<TimeSlotWithBookedResponse> timeSlots, Duration reservationLimitDuration) {
        return new ReservationInfo(timeSlots, reservationLimitDuration.toMinutes());
    }
}
