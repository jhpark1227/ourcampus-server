package com.umc.ourcampus.facility.presentation.dto.request;

import com.umc.ourcampus.facility.application.dto.request.UpdateFacilityRequest;
import com.umc.ourcampus.facility.domain.FacilityCategory;
import java.util.List;

public record UpdateFacilityWebRequest(
        String name,
        String description,
        String purpose,
        String equipment,
        String caution,
        String location,
        String thumbnailImage,
        FacilityCategory category,
        List<OperationTimeRequest> operationTimes,
        List<String> images,
        ReservationPolicyRequest reservationPolicy,
        Long buildingId,
        List<Long> themeIds
) {
    private record ReservationPolicyRequest(
            boolean reservable,
            List<AvailableTimeRequest> availableTimes
    ) {
    }

    private record AvailableTimeRequest(String startTime, String endTime) {
    }

    public record OperationTimeRequest(
            String name,
            String startTime,
            String endTime
    ) {
    }

    public UpdateFacilityRequest toDto() {
        return new UpdateFacilityRequest(
                name(),
                description(),
                purpose(),
                equipment(),
                caution(),
                location(),
                thumbnailImage(),
                category(),
                operationTimes()
                        .stream()
                        .map(o -> {
                            String[] parts = o.startTime().split(":");
                            int startTimeMinuteOffset = Integer.parseInt(parts[0]) * 60 + Integer.parseInt(parts[1]);
                            parts = o.endTime().split(":");
                            int endTimeMinuteOffset = Integer.parseInt(parts[0]) * 60 + Integer.parseInt(parts[1]);
                            return new UpdateFacilityRequest.OperationTimeRequest(o.name, startTimeMinuteOffset, endTimeMinuteOffset);
                        })
                        .toList(),
                images(),
                new UpdateFacilityRequest.ReservationPolicyRequest(reservationPolicy.reservable, reservationPolicy.availableTimes.stream()
                        .map(o -> {
                            String[] parts = o.startTime().split(":");
                            int startTimeMinuteOffset = Integer.parseInt(parts[0]) * 60 + Integer.parseInt(parts[1]);
                            parts = o.endTime().split(":");
                            int endTimeMinuteOffset = Integer.parseInt(parts[0]) * 60 + Integer.parseInt(parts[1]);
                            return new UpdateFacilityRequest.AvailableTimeRequest(startTimeMinuteOffset, endTimeMinuteOffset);
                        })
                        .toList()),
                buildingId,
                themeIds
        );
    }
}
