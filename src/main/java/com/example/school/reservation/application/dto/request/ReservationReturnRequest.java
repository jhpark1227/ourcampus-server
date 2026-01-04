package com.example.school.reservation.application.dto.request;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record ReservationReturnRequest(
        @NotEmpty
        List<String> images
) {
}
