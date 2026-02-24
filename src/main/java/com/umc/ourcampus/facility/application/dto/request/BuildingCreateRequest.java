package com.umc.ourcampus.facility.application.dto.request;

import java.time.LocalTime;
import java.util.List;

public record BuildingCreateRequest(
        String name,
        String label,
        double latitude,
        double longitude,
        String thumbnailImage,
        List<String> images,
        List<OperationTimeRequest> operationTimes
) {
    public record OperationTimeRequest(
            String name,
            LocalTime startTime,
            LocalTime endTime
    ) {
    }
}
