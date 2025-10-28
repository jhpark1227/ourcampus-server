package com.example.school.facility.domain;

import com.example.school.global.domain.BaseEntity;
import com.example.school.reservation.domain.Reservation;
import com.example.school.review.domain.Review;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Facility extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(mappedBy = "facility", cascade = CascadeType.ALL)
    private List<Reservation> reservationList = new ArrayList<>();

    @OneToMany(mappedBy = "facility", cascade = CascadeType.ALL)
    private List<Review> reviewList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id")
    private School school;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "building_id")
    private Building building;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theme_id")
    private Theme theme;

    private String name;
    private String extraName;
    private String imageURL;
    private String purpose;
    private String item;
    private String time;
    private String caution;
    private String location;
    private Double score;
    private Boolean isTheme;

    private String description;

    @Enumerated(EnumType.STRING)
    private FacilityTag tag;

    @Enumerated(EnumType.STRING)
    private FacilityKeyword keyword;

    @OneToMany(mappedBy = "facility", cascade = CascadeType.ALL)
    private List<FacilityHour> facilityHours = new ArrayList<>();

    public void updateScore(Double newScore) {
        score = newScore;
    }
}
