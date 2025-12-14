package com.example.school.member.converter;

import com.example.school.review.domain.Review;
import com.example.school.review.domain.ReviewImage;
import com.example.school.member.application.dto.MemberRequestDTO;
import com.example.school.member.application.dto.MemberResponseDTO;
import com.example.school.member.domain.Inquiry;
import com.example.school.member.domain.Member;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;

public class UserConverter {
    public static Review toReview(MemberRequestDTO.ReviewDTO request) {
        return Review.builder()
                .title(request.getTitle())
                .score(request.getScore())
                .body(request.getBody())
                .build();
    }

    public static MemberResponseDTO.CreateReviewResultDTO toCreateReviewResultDTO(Review review) {
        return MemberResponseDTO.CreateReviewResultDTO.builder()
                .reviewId(review.getId())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static MemberResponseDTO.ReviewPreViewDTO reviewPreViewDTO(Review review) {
        List<String> imageUrls = review.getReviewImages().stream()
                .map(ReviewImage::getImageUrl)
                .collect(Collectors.toList());

        return MemberResponseDTO.ReviewPreViewDTO.builder()
                .title(review.getTitle())
                .ownerNickname(review.getMember().getName())
                .profileUrl(review.getMember().getProfileImage())
                .score(review.getScore())
                .createdAt(review.getCreatedAt().toLocalDate())
                .body(review.getBody())
                .imageUrls(imageUrls)
                .build();
    }

    public static MemberResponseDTO.ReviewPreViewListDTO reviewPreViewListDTO(Page<Review> reviewList) {

        List<MemberResponseDTO.ReviewPreViewDTO> reviewPreViewDTOList = reviewList.stream()
                .map(UserConverter::reviewPreViewDTO).collect(Collectors.toList());

        return MemberResponseDTO.ReviewPreViewListDTO.builder()
                .isLast(reviewList.isLast())
                .isFirst(reviewList.isFirst())
                .totalPage(reviewList.getTotalPages())
                .totalElements(reviewList.getTotalElements())
                .listSize(reviewPreViewDTOList.size())
                .reviewList(reviewPreViewDTOList)
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

    public static MemberResponseDTO.UpdateReviewResultDTO toUpdateReviewResultDTO(Review updatedReview) {
        return MemberResponseDTO.UpdateReviewResultDTO.builder()
                .updatedReviewId(updatedReview.getId())
                .updatedScore(updatedReview.getScore())
                .updatedBody(updatedReview.getBody())
                .updatedTitle(updatedReview.getTitle())
                .updatedAt(LocalDateTime.now())
                .build();
    }

}
