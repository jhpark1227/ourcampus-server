package com.example.school.review.presentation;

import com.example.school.auth.domain.MemberPrincipal;
import com.example.school.review.application.ReviewService;
import com.example.school.review.application.dto.request.ReviewRequest;
import com.example.school.review.application.dto.response.ReviewResponse;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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

    @GetMapping("/me/reviews")
    public List<ReviewResponse> getMyReviews(@AuthenticationPrincipal MemberPrincipal memberPrincipal) {
        return reviewService.findReviewsByMemberId(memberPrincipal.memberId());
    }
}
