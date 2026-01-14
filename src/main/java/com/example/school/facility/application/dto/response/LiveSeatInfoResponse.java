package com.example.school.facility.application.dto.response;

import com.example.school.facility.domain.Facility;
import com.example.school.facility.domain.LiveSeatInfo;

public record LiveSeatInfoResponse(
        int totalSeats,
        int occupiedSeats,
        FacilityResponse facility
) {
    public static LiveSeatInfoResponse from(LiveSeatInfo liveSeatInfo) {
        return new LiveSeatInfoResponse(
                liveSeatInfo.getTotalSeats(),
                liveSeatInfo.getOccupiedSeats(),
                FacilityResponse.from(liveSeatInfo.getFacility())
        );
    }

    private record FacilityResponse(
            long id,
            String name,
            String location
    ) {
        private static FacilityResponse from(Facility facility) {
            return new FacilityResponse(
                    facility.getId(),
                    facility.getName(),
                    facility.getLocation()
            );
        }
    }
}
