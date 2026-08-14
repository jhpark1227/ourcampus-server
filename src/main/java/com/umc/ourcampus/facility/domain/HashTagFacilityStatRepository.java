package com.umc.ourcampus.facility.domain;

import com.umc.ourcampus.review.domain.HashTag;
import com.umc.ourcampus.university.domain.University;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HashTagFacilityStatRepository extends JpaRepository<HashTagFacilityStat, Long> {

    List<HashTagFacilityStat> findByUniversityAndHashTagOrderByRankAsc(University university, HashTag hashTag);

    void deleteAllByUniversity(University university);
}