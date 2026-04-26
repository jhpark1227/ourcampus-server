package com.umc.ourcampus.facility.domain;

import com.umc.ourcampus.review.domain.HashTag;
import com.umc.ourcampus.university.domain.University;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.Getter;

@Entity
@Getter
public class HashTagFacilityStat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "university_id", nullable = false)
    private University university;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hash_tag_id", nullable = false)
    private HashTag hashTag;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "facility_id", nullable = false)
    private Facility facility;

    @Column(name = "`rank`", nullable = false)
    private int rank;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public static HashTagFacilityStat of(University university, HashTag hashTag, Facility facility, int rank) {
        HashTagFacilityStat stat = new HashTagFacilityStat();
        stat.university = university;
        stat.hashTag = hashTag;
        stat.facility = facility;
        stat.rank = rank;
        stat.updatedAt = LocalDateTime.now();
        return stat;
    }
}
