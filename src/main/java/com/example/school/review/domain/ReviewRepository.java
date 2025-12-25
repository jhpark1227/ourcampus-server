package com.example.school.review.domain;

import com.example.school.member.domain.Member;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByMemberOrderByCreatedAt(Member member);
}