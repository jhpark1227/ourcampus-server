package com.example.school.review.application;

import com.example.school.facility.domain.Facility;
import com.example.school.facility.domain.FacilityRepository;
import com.example.school.file.application.FileManager;
import com.example.school.global.apiPayload.status.ErrorStatus;
import com.example.school.global.exception.ApplicationException;
import com.example.school.member.domain.Member;
import com.example.school.member.domain.MemberRepository;
import com.example.school.reservation.domain.Reservation;
import com.example.school.reservation.domain.ReservationRepository;
import com.example.school.review.application.dto.request.ReviewModifyRequest;
import com.example.school.review.application.dto.request.ReviewRequest;
import com.example.school.review.application.dto.response.ReviewResponse;
import com.example.school.review.domain.Review;
import com.example.school.review.domain.ReviewRepository;
import com.example.school.review.domain.StarRating;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReservationRepository reservationRepository;
    private final FacilityRepository facilityRepository;
    private final FileManager fileManager;
    private final MemberRepository memberRepository;

    public Long saveReview(ReviewRequest request, Long memberId) {
        Reservation reservation = reservationRepository.findById(request.reservationId())
                .orElseThrow(() -> new ApplicationException(ErrorStatus.RESERVATION_NOT_FOUND));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ApplicationException(ErrorStatus.MEMBER_NOT_FOUND));
        reservation.validateOwner(member);
        validateImages(request.images());

        Review review = new Review(request.content(), new StarRating(request.starRating()), request.images(), reservation);
        return reviewRepository.save(review).getId();
    }

    public List<ReviewResponse> findReviewsByMemberId(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ApplicationException(ErrorStatus.MEMBER_NOT_FOUND));
        return reviewRepository.findByMemberOrderByCreatedAt(member)
                .stream()
                .map(ReviewResponse::from)
                .toList();
    }

    public ReviewResponse findReviewById(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(ErrorStatus.REVIEW_NOT_FOUND));
        return ReviewResponse.from(review);
    }

    public void modifyReview(Long memberId, Long reviewId, ReviewModifyRequest request) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ApplicationException(ErrorStatus.REVIEW_NOT_FOUND));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ApplicationException(ErrorStatus.MEMBER_NOT_FOUND));
        review.validateOwner(member);
        validateImages(request.images());

        review.modify(request.content(), new StarRating(request.starRating()), request.images());
    }

    private void validateImages(List<String> images) {
        if (images.stream().anyMatch(imageUrl -> !fileManager.exist(imageUrl))) {
            throw new ApplicationException(ErrorStatus.IMAGE_NOT_FOUND);
        }
    }

    public void deleteReviewById(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ApplicationException(ErrorStatus.REVIEW_NOT_FOUND));
        reviewRepository.delete(review);
    }

    public List<ReviewResponse> findReviewByFacilityId(long facilityId, int page, int size) {
        Facility facility = facilityRepository.findById(facilityId)
                .orElseThrow(() -> new ApplicationException(ErrorStatus.FACILITY_NOT_FOUND));
        return reviewRepository.findByFacility(facility, PageRequest.of(page, size))
                .stream()
                .map(ReviewResponse::from)
                .toList();
    }
}
