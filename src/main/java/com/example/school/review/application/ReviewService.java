package com.example.school.review.application;

import com.example.school.file.application.FileManager;
import com.example.school.global.apiPayload.status.ErrorStatus;
import com.example.school.global.exception.ApplicationException;
import com.example.school.member.domain.Member;
import com.example.school.member.domain.MemberRepository;
import com.example.school.reservation.domain.Reservation;
import com.example.school.reservation.domain.ReservationRepository;
import com.example.school.review.application.dto.request.ReviewRequest;
import com.example.school.review.application.dto.response.ReviewResponse;
import com.example.school.review.domain.Review;
import com.example.school.review.domain.ReviewRepository;
import com.example.school.review.domain.StarRating;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReservationRepository reservationRepository;
    private final FileManager fileManager;
    private final MemberRepository memberRepository;

    @Transactional
    public Long saveReview(ReviewRequest request, Long memberId) {
        Reservation reservation = reservationRepository.findById(request.reservationId())
                .orElseThrow(() -> new ApplicationException(ErrorStatus.RESERVATION_NOT_FOUND));
        reservation.validateOwner(memberId);
        validateImageUrls(request.imageUrls());

        Review review = new Review(request.content(), new StarRating(request.starRating()), request.imageUrls(), reservation);
        return reviewRepository.save(review).getId();
    }

    private void validateImageUrls(List<String> imageUrls) {
        if (imageUrls.stream().anyMatch(imageUrl -> !fileManager.exist(imageUrl))) {
            throw new ApplicationException(ErrorStatus.IMAGE_NOT_FOUND);
        }
    }

    public List<ReviewResponse> findReviewsByMemberId(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ApplicationException(ErrorStatus.MEMBER_NOT_FOUND));
        return reviewRepository.findByMemberOrderByCreatedAt(member)
                .stream()
                .map(ReviewResponse::from)
                .toList();
    }
}
