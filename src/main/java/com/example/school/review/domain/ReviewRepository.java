package com.example.school.review.domain;

import com.example.school.facility.domain.Facility;
import com.example.school.member.domain.Member;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByReservation_MemberOrderByCreatedAt(Member member);

    List<Review> findByReservation_Facility(Facility facility, Pageable page);
}