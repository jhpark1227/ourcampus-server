package com.example.school.review.application;

import com.example.school.facility.domain.Facility;
import com.example.school.facility.domain.FacilityRepository;
import com.example.school.member.application.dto.MemberRequestDTO;
import com.example.school.member.application.dto.MemberResponseDTO;
import com.example.school.member.application.dto.MemberResponseDTO.ReviewPreViewDTO;
import com.example.school.member.converter.UserConverter;
import com.example.school.member.domain.Member;
import com.example.school.member.domain.MemberRepository;
import com.example.school.member.domain.ReviewImageRepository;
import com.example.school.member.domain.ReviewRepository;
import com.example.school.member.infrastructure.S3ProfileImageUploader;
import com.example.school.review.domain.Review;
import com.example.school.review.domain.ReviewImage;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final MemberRepository memberRepository;
    private final FacilityRepository facilityRepository;
    private final ReviewRepository reviewRepository;
    private final S3ProfileImageUploader s3ProfileImageUploader;
    private final ReviewImageRepository reviewImageRepository;

    public Review createReview(List<MultipartFile> imgFile, Long memberId, Long facilityId,
                               MemberRequestDTO.ReviewDTO request) {

        Review review = UserConverter.toReview(request);

        review = reviewRepository.save(review);

        if (imgFile != null && !imgFile.isEmpty()) {
            List<String> reviewImgUrls = s3ProfileImageUploader.uploadFiles(imgFile);
            for (String imageUrl : reviewImgUrls) {
                ReviewImage reviewImage = new ReviewImage();
                reviewImage.setReview(review);
                reviewImage.setImageUrl(imageUrl);
                reviewImageRepository.save(reviewImage);
            }
        }

        return review;
    }

    public Review updateReview(Long memberId, Long facilityId, Long reviewId, MemberRequestDTO.UpdateReviewDTO request,
                               List<MultipartFile> imgFile) {

        Review existingReview = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found with id: " + reviewId));

        validateReviewOwnership(existingReview, memberId, facilityId);

        // Update review content if request is not null

        // Update review images if imgFile is not null and not empty
        if (imgFile != null && !imgFile.isEmpty()) {
            // Delete previous images
            List<ReviewImage> oldImages = reviewImageRepository.findByReviewId(reviewId);
            for (ReviewImage oldImage : oldImages) {
                s3ProfileImageUploader.deleteFile(oldImage.getImageUrl());
                reviewImageRepository.delete(oldImage);
            }

            List<String> reviewImgUrls = s3ProfileImageUploader.uploadFiles(imgFile);
            for (String imageUrl : reviewImgUrls) {
                ReviewImage reviewImage = new ReviewImage();
                reviewImage.setReview(existingReview);
                reviewImage.setImageUrl(imageUrl);
                reviewImageRepository.save(reviewImage);
            }
        }

        return reviewRepository.save(existingReview);
    }

    public void deleteReview(Long memberId, Long facilityId, Long reviewId) {
        Review existingReview = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found with id: " + reviewId));
        validateReviewOwnership(existingReview, memberId, facilityId);

        reviewRepository.delete(existingReview);
    }

    public Page<ReviewPreViewDTO> findByFacility(Long facilityId, Integer page) {
        Facility facility = facilityRepository.findById(facilityId).get();
        Page<Review> facilityreviewpage = reviewRepository.findAllByFacility(facility, PageRequest.of(page - 1, 10));

        List<MemberResponseDTO.ReviewPreViewDTO> reviewPreViewDTOList = facilityreviewpage.stream()
                .map(UserConverter::reviewPreViewDTO)
                .collect(Collectors.toList());

        return new PageImpl<>(reviewPreViewDTOList, facilityreviewpage.getPageable(),
                facilityreviewpage.getTotalElements());

    }

    public Page<MemberResponseDTO.ReviewPreViewDTO> getAllReviewList(Integer page) {
        Page<Review> reviewPage = reviewRepository.findAll(PageRequest.of(page, 10));
        List<MemberResponseDTO.ReviewPreViewDTO> reviewPreViewDTOList = reviewPage.stream()
                .map(UserConverter::reviewPreViewDTO)
                .collect(Collectors.toList());

        return new PageImpl<>(reviewPreViewDTOList, reviewPage.getPageable(), reviewPage.getTotalElements());
    }

    public Page<MemberResponseDTO.ReviewPreViewDTO> getReviewList(Long MemberId, Integer page) {
        Member member = memberRepository.findById(MemberId).get();

        Page<Review> memberreviewpage = reviewRepository.findAllByMember(member, PageRequest.of(page, 10));
        List<MemberResponseDTO.ReviewPreViewDTO> reviewPreViewDTOList = memberreviewpage.stream()
                .map(UserConverter::reviewPreViewDTO)
                .collect(Collectors.toList());
        return new PageImpl<>(reviewPreViewDTOList, memberreviewpage.getPageable(),
                memberreviewpage.getTotalElements());
    }

    private void validateReviewOwnership(Review review, Long memberId, Long facilityId) {
        if (!review.getMember().getId().equals(memberId) || !review.getFacility().getId().equals(facilityId)) {
            throw new RuntimeException("Review does not belong to the specified member and facility.");
        }
    }

    public Optional<Review> findById(Long id) {
        return reviewRepository.findById(id);
    }
}
