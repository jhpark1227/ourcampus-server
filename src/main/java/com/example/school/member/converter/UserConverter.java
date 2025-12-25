package com.example.school.member.converter;

import com.example.school.member.application.dto.MemberRequestDTO;
import com.example.school.member.application.dto.MemberResponseDTO;
import com.example.school.member.domain.Inquiry;
import com.example.school.member.domain.Member;
import com.example.school.review.domain.Review;
import java.time.LocalDateTime;

public class UserConverter {

    public static MemberResponseDTO.CreateReviewResultDTO toCreateReviewResultDTO(Review review) {
        return MemberResponseDTO.CreateReviewResultDTO.builder()
                .reviewId(review.getId())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static Inquiry toInquiry(MemberRequestDTO.InquiryDTO request) {
        return Inquiry.builder()
                .title(request.getTitle())
                .body(request.getBody())
                .build();
    }

    public static MemberResponseDTO.CreateInquiryResultDTO toCreateInquiryResultDTO(Inquiry inquiry) {
        return MemberResponseDTO.CreateInquiryResultDTO.builder()
                .inquiryId(inquiry.getId())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static MemberResponseDTO.UpdateProfileResultDTO toUpdateProfileResultDTO(Member member) {
        return MemberResponseDTO.UpdateProfileResultDTO.builder()
                .name(member.getName())
                .profilePicture(member.getProfileImage())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
