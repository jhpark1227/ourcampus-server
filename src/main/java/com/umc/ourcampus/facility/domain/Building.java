package com.umc.ourcampus.facility.domain;

import com.umc.ourcampus.global.domain.BaseEntity;
import com.umc.ourcampus.university.domain.University;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE building SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class Building extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private double latitude;

    private double longitude;

    private String label;

    private String thumbnailImage;

    @ElementCollection
    private List<String> images = new ArrayList<>();

    @ElementCollection
    private List<OperationTime> operationTimes = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "university_id")
    private University university;

    public static Building create(String name, double latitude, double longitude, String label, String thumbnailImage, List<String> images,
                                  List<OperationTime> operationTimes, University university) {
        Building building = new Building();
        building.name = name;
        building.latitude = latitude;
        building.longitude = longitude;
        building.label = label;
        building.thumbnailImage = thumbnailImage;
        building.images = images;
        building.operationTimes = operationTimes;
        building.university = university;
        return building;
    }

    public void update(String name, double latitude, double longitude, String label, String thumbnailImage, List<String> images,
                       List<OperationTime> operationTimes) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.label = label;
        this.thumbnailImage = thumbnailImage;
        this.images = images;
        this.operationTimes = operationTimes;
    }
}
