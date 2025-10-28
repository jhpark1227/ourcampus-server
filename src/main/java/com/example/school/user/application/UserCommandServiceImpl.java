package com.example.school.user.application;

import com.example.school.facility.domain.FacilityRepository;
import com.example.school.global.awsS3.AwsS3Service;
import com.example.school.review.domain.Review;
import com.example.school.review.domain.ReviewImage;
import com.example.school.user.converter.UserConverter;
import com.example.school.user.domain.Inquiry;
import com.example.school.user.domain.Member;
import com.example.school.user.dto.UserRequestDTO;
import com.example.school.user.domain.InquiryRepository;
import com.example.school.user.domain.ReviewImageRepository;
import com.example.school.user.domain.ReviewRepository;
import com.example.school.user.domain.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@RequiredArgsConstructor
public class UserCommandServiceImpl implements UserCommandService {

    private final UserRepository userRepository;
    private final FacilityRepository facilityRepository;
    private final ReviewRepository reviewRepository;
    private final InquiryRepository inquiryRepository;
    private final AwsS3Service awsS3Service;
    private final ReviewImageRepository reviewImageRepository;


    @Override
    public Review createReview(List<MultipartFile> imgFile, Long memberId, Long facilityId,
                               UserRequestDTO.ReviewDTO request) {

        Review review = UserConverter.toReview(request);

        review.setMember(userRepository.findById(memberId).get());
        review.setFacility(facilityRepository.findById(facilityId).get());

        review = reviewRepository.save(review);

        if (imgFile != null && !imgFile.isEmpty()) {
            List<String> reviewImgUrls = awsS3Service.uploadFile(imgFile);
            for (String imageUrl : reviewImgUrls) {
                ReviewImage reviewImage = new ReviewImage();
                reviewImage.setReview(review);
                reviewImage.setImageUrl(imageUrl);
                reviewImageRepository.save(reviewImage);
            }
        }

        return review;
    }


    @Override
    public Review updateReview(Long memberId, Long facilityId, Long reviewId, UserRequestDTO.UpdateReviewDTO request,
                               List<MultipartFile> imgFile) {

        Review existingReview = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found with id: " + reviewId));

        validateReviewOwnership(existingReview, memberId, facilityId);

        // Update review content if request is not null
        if (request != null) {
            if (request.getTitle() != null) {
                existingReview.setTitle(request.getTitle());
            }
            if (request.getScore() != null) {
                existingReview.setScore(request.getScore());
            }
            if (request.getBody() != null) {
                existingReview.setBody(request.getBody());
            }
        }

        // Update review images if imgFile is not null and not empty
        if (imgFile != null && !imgFile.isEmpty()) {
            // Delete previous images
            List<ReviewImage> oldImages = reviewImageRepository.findByReviewId(reviewId);
            for (ReviewImage oldImage : oldImages) {
                awsS3Service.deleteFile(oldImage.getImageUrl());
                reviewImageRepository.delete(oldImage);
            }

            // Upload new images
            List<String> reviewImgUrls = awsS3Service.uploadFile(imgFile);
            for (String imageUrl : reviewImgUrls) {
                ReviewImage reviewImage = new ReviewImage();
                reviewImage.setReview(existingReview);
                reviewImage.setImageUrl(imageUrl);
                reviewImageRepository.save(reviewImage);
            }
        }

        return reviewRepository.save(existingReview);
    }

    private void validateReviewOwnership(Review review, Long memberId, Long facilityId) {
        if (!review.getMember().getId().equals(memberId) || !review.getFacility().getId().equals(facilityId)) {
            throw new RuntimeException("Review does not belong to the specified member and facility.");
        }
    }

    @Override
    public void deleteReview(Long memberId, Long facilityId, Long reviewId) {
        Review existingReview = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found with id: " + reviewId));
        validateReviewOwnership(existingReview, memberId, facilityId);

        reviewRepository.delete(existingReview);
    }

    @Override
    public Inquiry createInquiry(Long memberId, UserRequestDTO.InquiryDTO request) {

        Inquiry inquiry = UserConverter.toInquiry(request);

        inquiry.setMember(userRepository.findById(memberId).get());

        return inquiryRepository.save(inquiry);
    }

    @Override
    public Member updateProfile(Long memberId, UserRequestDTO.UpdateProfileDTO request, MultipartFile profileImg) {
        Member user = userRepository.findById(memberId).orElseThrow(() ->
                new RuntimeException("User not found with id: " + memberId));

        // 기존 프로필 사진 URL
        String existingProfilePictureUrl = user.getProfileImg();

        // 새로운 사진이 들어왔고, 기존 사진이 존재할 시 기존 사진 삭제
        if (existingProfilePictureUrl != null && profileImg != null) {
            String existingFileName = extractFileNameFromUrl(existingProfilePictureUrl);
            awsS3Service.deleteFile(existingFileName);
        }

        // 새 프로필 사진 업로드
        if (profileImg != null) {
            String newProfilePictureUrl = awsS3Service.uploadSingleFile(profileImg);
            user.setProfileImg(newProfilePictureUrl);
        }

        // 닉네임 변경
        if (request != null && request.getNickname() != null) {
            user.setNickname(request.getNickname());
        }

        return userRepository.save(user);
    }


    // URL에서 파일 이름을 추출하는 메서드
    private String extractFileNameFromUrl(String url) {
        int lastIndexOfSlash = url.lastIndexOf("/");
        return url.substring(lastIndexOfSlash + 1);
    }
}

