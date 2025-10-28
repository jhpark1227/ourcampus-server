package com.example.school.facility.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class FacilitySaveResponseDTO {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateFacilityResultDTO {
        String name;
        String address;
        Float score;
    }
}