package com.umc.ourcampus.facility.application.dto.response;

import com.umc.ourcampus.reservation.domain.SlotAvailability;
import com.umc.ourcampus.reservation.domain.TimeSlot;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record FacilityScheduleResponse(
        List<TimeSlotWithBooked> timeSlots,
        LocalDate date
) {
    public static FacilityScheduleResponse of(LocalDate date, List<SlotAvailability> slotAvailabilities) {
        return new FacilityScheduleResponse(
                slotAvailabilities.stream()
                        .map(TimeSlotWithBooked::from)
                        .toList(),
                date
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