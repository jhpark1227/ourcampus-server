package com.umc.ourcampus.facility.domain;

import com.umc.ourcampus.global.domain.BaseEntity;
import com.umc.ourcampus.reservation.domain.ReservationPolicy;
import com.umc.ourcampus.university.domain.University;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE facility SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class Facility extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private String purpose;

    private String equipment;

    private String caution;

    private String location;

    private String thumbnailImage;

    @Enumerated(EnumType.STRING)
    private FacilityCategory category;

    private ReservationPolicy reservationPolicy;

    @ElementCollection
    private List<OperationTime> operationTimes = new ArrayList<>();

    @ElementCollection
    private List<String> images = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "building_id")
    private Building building;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "university_id")
    private University university;

    public Facility(String name, String description, String purpose, String equipment, String caution, String location,
                    String thumbnailImage,
                    FacilityCategory category, ReservationPolicy reservationPolicy, List<OperationTime> operationTimes, List<String> images,
                    Building building, University university) {
        this.name = name;
        this.description = description;
        this.purpose = purpose;
        this.equipment = equipment;
        this.caution = caution;
        this.location = location;
        this.thumbnailImage = thumbnailImage;
        this.category = category;
        this.reservationPolicy = reservationPolicy;
        this.operationTimes = operationTimes;
        this.images = images;
        this.building = building;
        this.university = university;
    }

    public void update(String name, String description, String purpose, String equipment,
                       String caution, String location, String thumbnailImage, FacilityCategory category,
                       ReservationPolicy reservationPolicy,
                       List<OperationTime> operationTimes, List<String> images, Building building
    ) {
        this.name = name;
        this.description = description;
        this.purpose = purpose;
        this.equipment = equipment;
        this.caution = caution;
        this.location = location;
        this.thumbnailImage = thumbnailImage;
        this.category = category;
        this.operationTimes = operationTimes;
        this.images = images;
        this.building = building;
        this.reservationPolicy = reservationPolicy;
    }

    public Optional<Building> getBuilding() {
        return Optional.ofNullable(building);
    }

    public void clearBuilding() {
        this.building = null;
    }
}
