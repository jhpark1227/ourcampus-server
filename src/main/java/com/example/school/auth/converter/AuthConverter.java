package com.example.school.auth.converter;


import com.example.school.auth.application.dto.AuthRequestDTO;
import com.example.school.auth.application.dto.AuthResponseDTO;
import com.example.school.user.domain.Member;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class AuthConverter {

    // 로그인
    public static AuthResponseDTO.RegisterResDTO toRegisterResDTO(Member member) {
        return AuthResponseDTO.RegisterResDTO.builder()
                .nickname(member.getNickname())
                .build();
    }

    public static Member toMember(AuthRequestDTO.RegisterReqDTO request) {
        String encryptedPassword = new BCryptPasswordEncoder().encode(request.getPassword());

        return Member.builder()
                .name(request.getName())
                .userId(request.getUserId())
                .email(request.getEmail())
                .identifyNum(request.getIdentify_num())
                .password(encryptedPassword)
                .school(request.getSchool())
                .phone(request.getPhone())
                .nickname(request.getNickname())
                .age(request.getAge())
                .build();
    }

}
