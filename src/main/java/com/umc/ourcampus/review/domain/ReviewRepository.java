package com.umc.ourcampus.review.domain;

import com.umc.ourcampus.member.domain.Member;
import com.umc.ourcampus.facility.domain.Facility;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewRepositoryCustom {

    List<Review> findByMemberOrderByCreatedAt(Member member);

    void deleteByFacility(Facility facility);

    Page<Review> findByFacilityOrderByCreatedAtDesc(Facility facility, Pageable page);

    @Query("""
                SELECT IFNULL(AVG(review.starRating.value), 0)
                FROM Review review
                WHERE review.facility = :facility
            """)
    double findAverageStarRatingByFacility(Facility facility);
}