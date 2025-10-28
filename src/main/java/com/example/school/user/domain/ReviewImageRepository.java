package com.example.school.user.domain;


import com.example.school.review.domain.Review;
import com.example.school.review.domain.ReviewImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewImageRepository extends JpaRepository<ReviewImage, Long> {
    List<ReviewImage> findByReview(Review review);

    List<ReviewImage> findByReviewId(Long reviewId);

}