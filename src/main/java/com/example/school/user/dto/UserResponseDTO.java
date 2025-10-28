package com.example.school.user.dto;

import com.example.school.user.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class UserResponseDTO {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateReviewResultDTO{
        Long reviewId;
        LocalDateTime createdAt;
    }
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateReviewResultDTO {
        Long updatedReviewId;
        Float updatedScore;
        String updatedTitle;
        String updatedBody;
        LocalDateTime updatedAt;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReviewPreViewListDTO{
        List<UserResponseDTO.ReviewPreViewDTO> reviewList;
        Integer listSize;
        Integer totalPage;
        Long totalElements;
        Boolean isFirst;
        Boolean isLast;
    }
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReviewPreViewDTO{
        String title;
        String ownerNickname;
        String profileUrl;
        Float score;
        String body;
        LocalDate createdAt;
        List<String> imageUrls;
    }
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateInquiryResultDTO{
        Long inquiryId;
        LocalDateTime createdAt;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateProfileResultDTO {
        String nickname;
        String profilePicture;
        LocalDateTime updatedAt;
    }

    @Getter
    public static class Info {
        String name;
        String imageURL;
        Integer grade;


        public Info(Member member){
            name = member.getName();
            imageURL = member.getProfileImg();
            grade = member.getGrade();
        }
    }
}
