package com.example.school.facility.application.dto.response;

import com.example.school.facility.domain.OperationTime;

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
