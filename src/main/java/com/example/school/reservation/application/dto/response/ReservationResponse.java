package com.example.school.reservation.application.dto.response;

import com.example.school.facility.domain.Facility;
import com.example.school.member.domain.Member;
import com.example.school.reservation.domain.Reservation;
import java.time.LocalDateTime;

public record ReservationResponse(
        Long id,
        LocalDateTime startTime,
        long durationMinutes,
        int headCount,
        FacilityResponse facility,
        MemberResponse member
) {
    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getTimeSlot().startTime(),
                reservation.getTimeSlot().getDuration().toMinutes(),
                reservation.getHeadCount(),
                FacilityResponse.from(reservation.getFacility()),
                MemberResponse.from(reservation.getMember())
        );
    }

    private record FacilityResponse(
            Long id,
            String name,
            String description,
            String purpose,
            String equipment,
            String caution,
            String location,
            String imageUrl,
            long reservationDurationLimit
    ) {
        private static FacilityResponse from(Facility facility) {
            return new FacilityResponse(
                    facility.getId(),
                    facility.getName(),
                    facility.getDescription(),
                    facility.getPurpose(),
                    facility.getEquipment(),
                    facility.getCaution(),
                    facility.getLocation(),
                    facility.getThumbnailImage(),
                    facility.getReservationPolicy().getReservationDurationLimit().toMinutes()
            );
        }
    }

    private record MemberResponse(
            String name,
            String profileImage,
            Long universityId
    ) {
        private static MemberResponse from(Member member) {
            return new MemberResponse(member.getName(), member.getProfileImage(), member.getUniversity().getId());
        }
    }
}
