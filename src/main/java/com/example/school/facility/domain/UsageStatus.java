package com.example.school.facility.domain;

import com.example.school.global.domain.BaseEntity;
import com.example.school.university.domain.University;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UsageStatus extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String facilityName;

    private int totalSeats;

    private int occupiedSeats;

    @ManyToOne(fetch = FetchType.LAZY)
    private University university;

    public static UsageStatus create(String facilityName, int totalSeats, int occupiedSeats, University university) {
        UsageStatus usageStatus = new UsageStatus();
        usageStatus.facilityName = facilityName;
        usageStatus.totalSeats = totalSeats;
        usageStatus.occupiedSeats = occupiedSeats;
        usageStatus.university = university;
        return usageStatus;
    }
}
