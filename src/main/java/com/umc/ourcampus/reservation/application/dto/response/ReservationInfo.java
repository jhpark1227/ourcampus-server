package com.umc.ourcampus.reservation.application.dto.response;

import com.umc.ourcampus.reservation.domain.SlotAvailability;
import com.umc.ourcampus.reservation.domain.TimeSlot;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public record ReservationInfo(
        List<TimeSlotWithBooked> timeSlots,
        long reservationLimitMinutes
) {
    public static ReservationInfo of(List<SlotAvailability> slotAvailabilities, Duration reservationLimitDuration) {
        return new ReservationInfo(
                slotAvailabilities.stream()
                        .map(TimeSlotWithBooked::from)
                        .toList(),
                reservationLimitDuration.toMinutes()
        );
    }

    public record TimeSlotWithBooked(
            LocalDateTime startTime,
            long durationMinutes,
            boolean isBooked
    ) {
        public static TimeSlotWithBooked from(SlotAvailability slotAvailability) {
            TimeSlot timeSlot = slotAvailability.timeSlot();
            return new TimeSlotWithBooked(
                    timeSlot.startTime(),
                    timeSlot.getDuration().toMinutes(),
                    slotAvailability.booked()
            );
        }
    }
}