package com.umc.ourcampus.review.application;

import com.umc.ourcampus.reservation.domain.Reservation;
import com.umc.ourcampus.facility.domain.Facility;
import com.umc.ourcampus.facility.domain.FacilityRepository;
import com.umc.ourcampus.file.application.FileManager;
import com.umc.ourcampus.global.exception.ErrorStatus;
import com.umc.ourcampus.global.exception.ApplicationException;
import com.umc.ourcampus.member.domain.Member;
import com.umc.ourcampus.member.domain.MemberRepository;
import com.umc.ourcampus.reservation.domain.ReservationRepository;
import com.umc.ourcampus.review.application.dto.request.ReviewModifyRequest;
import com.umc.ourcampus.review.application.dto.request.ReviewRequest;
import com.umc.ourcampus.review.application.dto.response.ReviewResponse;
import com.umc.ourcampus.review.application.dto.response.ReviewStatisticsResponse;
import com.umc.ourcampus.review.domain.HashTag;
import com.umc.ourcampus.review.domain.HashTagRepository;
import com.umc.ourcampus.review.domain.Review;
import com.umc.ourcampus.review.domain.ReviewRepository;
import com.umc.ourcampus.review.domain.ReviewStarRatingCount;
import com.umc.ourcampus.review.domain.StarRating;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final HashTagRepository hashTagRepository;

    public Long saveReview(ReviewRequest request, Long memberId) {
        Reservation reservation = reservationRepository.findById(request.reservationId())
                .orElseThrow(() -> new ApplicationException(ErrorStatus.RESERVATION_NOT_FOUND));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ApplicationException(ErrorStatus.MEMBER_NOT_FOUND));
        List<HashTag> hashTags = request.hashTagIds()
                .stream()
                .map(id -> hashTagRepository.findById(id).orElseThrow(() -> new ApplicationException(ErrorStatus.NOTICE_NOT_FOUND)))
                .toList();
        reservation.validateOwner(member);
        validateImages(request.images());

        Review review = new Review(
                request.content(),
                new StarRating(request.starRating()),
                request.images(),
                hashTags,
                reservation
        );
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
        List<HashTag> hashTags = request.hashTagIds()
                .stream()
                .map(id -> hashTagRepository.findById(id).orElseThrow(() -> new ApplicationException(ErrorStatus.NOTICE_NOT_FOUND)))
                .toList();
        review.validateOwner(member);
        validateImages(request.images());

        review.modify(request.content(), new StarRating(request.starRating()), request.images(), hashTags);
    }

    public void deleteReviewById(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ApplicationException(ErrorStatus.REVIEW_NOT_FOUND));
        reviewRepository.delete(review);
    }

    public Page<ReviewResponse> findReviewByFacilityId(long facilityId, Pageable pageable) {
        Facility facility = facilityRepository.findById(facilityId)
                .orElseThrow(() -> new ApplicationException(ErrorStatus.FACILITY_NOT_FOUND));
        return reviewRepository.findByFacilityOrderByCreatedAtDesc(facility, pageable)
                .map(ReviewResponse::from);
    }

    private void validateImages(List<String> images) {
        if (images.stream().anyMatch(imageUrl -> !fileManager.exist(imageUrl))) {
            throw new ApplicationException(ErrorStatus.IMAGE_NOT_FOUND);
        }
    }

    public ReviewStatisticsResponse getReviewStatistics(long facilityId) {
        Facility facility = facilityRepository.findById(facilityId)
                .orElseThrow(() -> new ApplicationException(ErrorStatus.FACILITY_NOT_FOUND));
        List<ReviewStarRatingCount> starRatingCounts = reviewRepository.getStarRatingCounts(facility);
        return ReviewStatisticsResponse.of(starRatingCounts);
    }
}
