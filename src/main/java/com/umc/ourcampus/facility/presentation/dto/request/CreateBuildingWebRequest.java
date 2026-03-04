package com.umc.ourcampus.facility.presentation.dto.request;

import com.umc.ourcampus.facility.application.dto.request.BuildingCreateRequest;
import java.util.List;

public record CreateBuildingWebRequest(
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
            String startTime,
            String endTime
    ) {
    }

    public BuildingCreateRequest toDto() {
        return new BuildingCreateRequest(
                name,
                label,
                latitude,
                longitude,
                thumbnailImage,
                images,
                operationTimes.stream()
                        .map(o -> {
                            String[] parts = o.startTime().split(":");
                            int startTimeMinuteOffset = Integer.parseInt(parts[0]) * 60 + Integer.parseInt(parts[1]);
                            parts = o.endTime().split(":");
                            int endTimeMinuteOffset = Integer.parseInt(parts[0]) * 60 + Integer.parseInt(parts[1]);
                            return new BuildingCreateRequest.OperationTimeRequest(o.name(), startTimeMinuteOffset, endTimeMinuteOffset);
                        })
                        .toList()
        );
    }
}
