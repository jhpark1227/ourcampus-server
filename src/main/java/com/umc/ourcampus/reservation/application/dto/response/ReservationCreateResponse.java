package com.umc.ourcampus.reservation.application.dto.response;

import com.umc.ourcampus.member.domain.Member;
import com.umc.ourcampus.facility.domain.Facility;
import com.umc.ourcampus.reservation.domain.Reservation;
import java.time.LocalDateTime;
import java.util.List;

public record ReservationCreateResponse(
        Long id,
        MemberResponse member,
        FacilityResponse facility,
        LocalDateTime startTime,
        LocalDateTime endTime
) {
    public static ReservationCreateResponse from(Reservation reservation) {
        return new ReservationCreateResponse(
                reservation.getId(),
                MemberResponse.from(reservation.getMember()),
                FacilityResponse.from(reservation.getFacility()),
                reservation.getTimeSlot().startTime(),
                reservation.getTimeSlot().endTime()
        );
    }

    private record MemberResponse(
            String name,
            String studentId,
            String profileImage,
            String department
    ) {
        private static MemberResponse from(Member member) {
            return new MemberResponse(
                    member.getName(),
                    member.getStudentId(),
                    member.getProfileImage(),
                    member.getDepartment().getName()
            );
        }
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
            List<String> images
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
                    facility.getImages()
            );
        }
    }
}
