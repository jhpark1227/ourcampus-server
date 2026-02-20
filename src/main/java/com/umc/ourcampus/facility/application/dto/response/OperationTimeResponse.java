package com.umc.ourcampus.facility.application.dto.response;

import com.umc.ourcampus.facility.domain.OperationTime;

public record OperationTimeResponse(
        String name,
        String startTime,
        String endTime
) {
    public static OperationTimeResponse from(OperationTime operationTime) {
        return new OperationTimeResponse(
                operationTime.name(),
                operationTime.startTime().toDisplayString(),
                operationTime.endTime().toDisplayString()
        );
    }
}
