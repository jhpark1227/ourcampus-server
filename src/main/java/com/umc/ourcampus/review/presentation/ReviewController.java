package com.umc.ourcampus.review.presentation;

import com.umc.ourcampus.auth.domain.MemberPrincipal;
import com.umc.ourcampus.review.application.ReviewService;
import com.umc.ourcampus.review.application.dto.request.ReviewModifyRequest;
import com.umc.ourcampus.review.application.dto.request.ReviewRequest;
import com.umc.ourcampus.review.application.dto.response.ReviewResponse;
import com.umc.ourcampus.review.application.dto.response.ReviewStatisticsResponse;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/reviews")
    public ResponseEntity<Void> createReview(@RequestBody ReviewRequest request, @AuthenticationPrincipal MemberPrincipal memberPrincipal) {
        Long reviewId = reviewService.saveReview(request, memberPrincipal.memberId());
        return ResponseEntity.created(URI.create("/reviews/" + reviewId))
                .build();
    }

    @GetMapping("/facilities/{facilityId}/reviews")
    public Page<ReviewResponse> getFacilityReviews(
            @PathVariable("facilityId") long facilityId,
            Pageable pageable
    ) {
        return reviewService.findReviewByFacilityId(facilityId, pageable);
    }

    @GetMapping("/reviews/{reviewId}")
    public ReviewResponse getReview(@PathVariable("reviewId") Long reviewId) {
        return reviewService.findReviewById(reviewId);
    }

    @GetMapping("/me/reviews")
    public List<ReviewResponse> getMyReviews(@AuthenticationPrincipal MemberPrincipal memberPrincipal) {
        return reviewService.findReviewsByMemberId(memberPrincipal.memberId());
    }

    @GetMapping("/facilities/{facilityId}/reviews/statistics")
    public ReviewStatisticsResponse getReviewStatistics(@PathVariable("facilityId") Long facilityId) {
        return reviewService.getReviewStatistics(facilityId);
    }

    @PutMapping("/reviews/{reviewId}")
    public ResponseEntity<Void> modifyReview(
            @AuthenticationPrincipal MemberPrincipal memberPrincipal,
            @PathVariable("reviewId") Long reviewId,
            @RequestBody ReviewModifyRequest request
    ) {
        reviewService.modifyReview(memberPrincipal.memberId(), reviewId, request);
        return ResponseEntity.noContent()
                .build();
    }

    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @AuthenticationPrincipal MemberPrincipal memberPrincipal,
            @PathVariable("reviewId") Long reviewId
    ) {
        reviewService.deleteReviewById(reviewId);
        return ResponseEntity.noContent()
                .build();
    }
}
