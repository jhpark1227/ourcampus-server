package com.example.school.facility.application.dto.response;

import com.example.school.facility.domain.UsageStatus;

public record UsageStatusResponse(
        String facilityName,
        int totalSeats,
        int occupiedSeats
) {
    public static UsageStatusResponse from(UsageStatus usageStatus) {
        return new UsageStatusResponse(
                usageStatus.getFacilityName(),
                usageStatus.getTotalSeats(),
                usageStatus.getOccupiedSeats()
        );
    }
}
