package com.umc.ourcampus.facility.application.dto.response;

import com.umc.ourcampus.reservation.application.dto.response.TimeSlotWithBookedResponse;
import java.time.LocalDate;
import java.util.List;

public record FacilityScheduleResponse(
        List<TimeSlotWithBookedResponse> timeSlots,
        LocalDate date
) {
}
