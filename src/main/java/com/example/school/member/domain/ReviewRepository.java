package com.example.school.member.domain;

import com.example.school.facility.domain.Facility;
import com.example.school.review.domain.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Page<Review> findAllByFacility(Facility facility, PageRequest pageRequest);

    Page<Review> findAllByMember(Member member, PageRequest pageRequest);

    Page<Review> findAll(Pageable pageable);


}