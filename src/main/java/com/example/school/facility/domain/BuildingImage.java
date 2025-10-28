package com.example.school.facility.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder @NoArgsConstructor(access = AccessLevel.PROTECTED) @AllArgsConstructor
public class BuildingImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String imageURL;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "building_id")
    Building building;
}
