package com.example.school.review.domain;

import com.example.school.facility.domain.Facility;
import com.example.school.facility.domain.ReservableFacility;
import com.example.school.member.domain.Member;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewRepositoryCustom {

    List<Review> findByReservation_MemberOrderByCreatedAt(Member member);

    Page<Review> findByReservation_FacilityOrderByCreatedAtDesc(ReservableFacility facility, Pageable page);

    @Query("""
                SELECT AVG(review.starRating.value)
                FROM Review review
                WHERE review.reservation.facility = :facility
            """)
    double findAverageStarRatingByFacility(Facility facility);
}