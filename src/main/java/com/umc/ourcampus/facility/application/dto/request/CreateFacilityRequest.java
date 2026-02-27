package com.umc.ourcampus.facility.application.dto.request;

import com.umc.ourcampus.facility.domain.FacilityCategory;
import com.umc.ourcampus.facility.domain.MinuteOffset;
import com.umc.ourcampus.facility.domain.OperationTime;
import com.umc.ourcampus.reservation.domain.AvailableTime;
import com.umc.ourcampus.reservation.domain.ReservationPolicy;
import java.util.List;

public record CreateFacilityRequest(
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
    public record ReservationPolicyRequest(
            boolean reservable,
            List<AvailableTimeRequest> availableTimes
    ) {
    }

    public record AvailableTimeRequest(int startTimeMinuteOffset, int endTimeMinuteOffset) {
    }

    public record OperationTimeRequest(
            String name,
            int startTimeMinuteOffset,
            int endTimeMinuteOffset
    ) {
    }

    public ReservationPolicy toReservationPolicy() {
        List<AvailableTime> availableTimes = reservationPolicy
                .availableTimes()
                .stream()
                .map(t -> new AvailableTime(new MinuteOffset(t.startTimeMinuteOffset()), new MinuteOffset(t.endTimeMinuteOffset())))
                .toList();
        return new ReservationPolicy(reservationPolicy.reservable(), availableTimes);
    }

    public List<OperationTime> toOperationTimes() {
        return operationTimes.stream()
                .map(o -> new OperationTime(o.name, new MinuteOffset(o.startTimeMinuteOffset), new MinuteOffset(o.endTimeMinuteOffset)))
                .toList();
    }
}
