package com.example.school.auth.application.dto;

import com.example.school.facility.domain.School;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class AuthRequestDTO {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    //회원 가입
    public static class RegisterReqDTO {
        @NotBlank(message = "이름을 입력해 주세요.")
        String name;
        @NotBlank(message = "이메일을 입력해 주세요.")
        String email;
        @NotBlank(message = "비밀번호를 입력해 주세요.")
        String password;
        @NotBlank(message = "주민번호를 입력해 주세요.")
        String identify_num;
        String phone;
        @NotBlank(message = "아이디를 입력해 주세요.")
        String userId;
        String nickname;
        String profileImg;

        Integer age;
        Integer grade;
        School school;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginReqDTO {

        String userId;
        String password;

        public UsernamePasswordAuthenticationToken toAuthentication() {
            return new UsernamePasswordAuthenticationToken(this.userId, this.password);
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmailAuthReqDTO {

        String email;
        String authCode;
    }


    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ChangePasswordReqDTO {
        private String token;
        private String currentPassword;
        private String changePassword;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FindPwRequest {
        private String email;
        private String userId;
        private String password;
        private String authCode;
    }
}
