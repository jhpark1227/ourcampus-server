package com.example.school.member.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

public class MemberRequestDTO {
    @Getter
    public static class ReviewDTO {
        @NotBlank
        String title;
        @NotNull
        Float score;
        @NotBlank
        String body;
    }

    @Getter
    public static class InquiryDTO {
        @NotBlank
        String title;
        @NotBlank
        String body;
    }

    @Getter
    public static class UpdateProfileDTO {
        String nickname;

    }

    @Getter
    public static class UpdateReviewDTO {

        String title;
        Float score;
        String body;
    }
}
