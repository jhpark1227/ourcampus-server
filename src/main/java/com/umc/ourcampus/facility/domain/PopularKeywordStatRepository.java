package com.umc.ourcampus.facility.domain;

import com.umc.ourcampus.university.domain.University;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PopularKeywordStatRepository extends JpaRepository<PopularKeywordStat, Long> {

    List<PopularKeywordStat> findByUniversityOrderByRankAsc(University university, Pageable pageable);

    void deleteAllByUniversity(University university);
}