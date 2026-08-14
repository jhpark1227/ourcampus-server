package com.umc.ourcampus.facility.domain;

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
public class PopularKeywordStat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "university_id", nullable = false)
    private University university;

    private SearchKeyword keyword;

    @Column(name = "`rank`", nullable = false)
    private int rank;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public static PopularKeywordStat of(University university, SearchKeyword keyword, int rank) {
        PopularKeywordStat stat = new PopularKeywordStat();
        stat.university = university;
        stat.keyword = keyword;
        stat.rank = rank;
        stat.updatedAt = LocalDateTime.now();
        return stat;
    }
}