package com.example.school.auth.application;

import com.example.school.auth.application.dto.AuthRequestDTO;
import com.example.school.auth.application.dto.AuthResponseDTO;
import com.example.school.auth.application.dto.request.LoginRequest;
import com.example.school.auth.application.dto.response.LoginResponse;
import com.example.school.auth.domain.RefreshToken;
import com.example.school.auth.domain.RefreshTokenRepository;
import com.example.school.global.apiPayload.GeneralException;
import com.example.school.global.apiPayload.status.ErrorStatus;
import com.example.school.global.exception.ApplicationException;
import com.example.school.member.application.PasswordEncoder;
import com.example.school.member.domain.Member;
import com.example.school.member.domain.MemberRepository;
import com.example.school.member.domain.Password;
import com.example.school.university.domain.University;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthQueryService {

    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public LoginResponse login(LoginRequest request) {
        Member member = memberRepository.findByEmail(request.email())
                .orElseThrow(() -> new ApplicationException(ErrorStatus.LOGIN_ERROR));

        if (!passwordEncoder.matches(request.password(), member.getPassword())) {
            throw new ApplicationException(ErrorStatus.LOGIN_ERROR);
        }

        String accessToken = jwtUtils.createAccessToken(member);
        String refreshToken = jwtUtils.createRefreshToken(member);
        refreshTokenRepository.save(new RefreshToken(refreshToken, member));

        return new LoginResponse(accessToken, refreshToken);
    }

    @Transactional
    public Boolean changePassword(AuthRequestDTO.ChangePasswordReqDTO request) {
        String email = jwtUtils.getEmailInToken(request.getToken());
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> {
            throw new GeneralException(ErrorStatus.MEMBER_NOT_FOUND);
        });
        //기존 비밀번호와 일치하는지 확인 후 맞을 시 변경
        if (passwordEncoder.matches(request.getCurrentPassword(), member.getPassword())) {
            member.changePassword(passwordEncoder, new Password(request.getChangePassword()));
            memberRepository.save(member);
            return true;
        } else {
            return false;
        }
    }

    public List<AuthResponseDTO.SchoolResDTO> searchSchool(String schoolName) {
        // 대학 검색 쿼리 수행
        List<University> universities = memberRepository.findSchoolByName(schoolName);

        // 검색된 대학이 없을 경우
        if (universities.isEmpty()) {
            // 또는 다른 처리 로직을 수행하거나 예외를 던질 수 있습니다.
            return Collections.emptyList();
        }

        // 검색된 대학들을 DTO로 매핑
        List<AuthResponseDTO.SchoolResDTO> schoolResDTOs = universities.stream()
                .map(school -> new AuthResponseDTO.SchoolResDTO(school.getId(), school.getName()))
                .collect(Collectors.toList());

        // 최종 결과 반환
        return schoolResDTOs;
    }
}
