package com.example.school.facility.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class FacilityRequestDTO {
    @Getter
    public static class ReviewDTO {
        @NotBlank
        String title;
        @NotNull
        Float score;
        @NotBlank
        String body;
    }
}
