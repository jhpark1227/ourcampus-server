package com.example.school.facility.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder @AllArgsConstructor @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FacilityImage {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String imageURL;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facility_id")
    Facility facility;
}
