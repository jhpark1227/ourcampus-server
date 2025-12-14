package com.example.school.facility.application.dto.response;

import com.example.school.reservation.application.dto.response.TimeSlotWithBookedResponse;
import java.time.LocalDate;
import java.util.List;

public record FacilityScheduleResponse(
        List<TimeSlotWithBookedResponse> timeSlots,
        LocalDate date
) {
}
