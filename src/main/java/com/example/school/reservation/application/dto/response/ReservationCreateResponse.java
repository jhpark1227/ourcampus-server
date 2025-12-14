package com.example.school.reservation.application.dto.response;

import com.example.school.facility.application.dto.response.FacilityResponse;
import com.example.school.member.application.dto.response.MemberInfoResponse;
import com.example.school.reservation.domain.Reservation;
import java.time.LocalDateTime;

public record ReservationCreateResponse(
        Long id,
        MemberInfoResponse member,
        FacilityResponse facility,
        LocalDateTime startTime,
        LocalDateTime endTime
) {
    public static ReservationCreateResponse from(Reservation reservation) {
        return new ReservationCreateResponse(
                reservation.getId(),
                MemberInfoResponse.from(reservation.getMember()),
                FacilityResponse.from(reservation.getFacility()),
                reservation.getTimeSlot().startTime(),
                reservation.getTimeSlot().endTime()
        );
    }
}
