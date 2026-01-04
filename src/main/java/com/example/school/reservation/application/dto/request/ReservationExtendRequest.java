package com.example.school.reservation.application.dto.request;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record ReservationExtendRequest(
        @NotNull
        LocalDateTime endTime
) {
}
