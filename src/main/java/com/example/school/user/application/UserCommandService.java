package com.example.school.user.application;

import com.example.school.review.domain.Review;
import com.example.school.user.domain.Inquiry;
import com.example.school.user.domain.Member;
import com.example.school.user.dto.UserRequestDTO;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface UserCommandService {
    Review createReview(List<MultipartFile> imgFile, Long memberId, Long facilityId, UserRequestDTO.ReviewDTO request);

    Review updateReview(Long memberId, Long facilityId, Long reviewId, UserRequestDTO.UpdateReviewDTO request,
                        List<MultipartFile> imgFile);

    void deleteReview(Long memberId, Long facilityId, Long reviewId);

    Inquiry createInquiry(Long memberId, UserRequestDTO.InquiryDTO request);

    Member updateProfile(Long memberId, UserRequestDTO.UpdateProfileDTO request, MultipartFile profileImg);
}
