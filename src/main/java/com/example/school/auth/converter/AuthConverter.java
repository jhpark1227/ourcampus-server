package com.example.school.auth.converter;


import com.example.school.auth.application.dto.AuthResponseDTO;
import com.example.school.member.domain.Member;

public class AuthConverter {

    // 로그인
    public static AuthResponseDTO.RegisterResDTO toRegisterResDTO(Member member) {
        return AuthResponseDTO.RegisterResDTO.builder()
                .nickname("nickname")
                .build();
    }
}
