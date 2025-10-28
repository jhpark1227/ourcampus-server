package com.example.school.facility.application.dto;

import com.example.school.facility.domain.Facility;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ScoreDTO {
    private Facility facility;
    private Double newScore;
}
