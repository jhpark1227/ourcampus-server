package com.example.school.facility.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder @AllArgsConstructor @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BuildingHour {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String openingTime;

    private String closingTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "building_id")
    private Building building;
}
