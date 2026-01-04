package com.example.school.auth.application;

import com.example.school.auth.application.dto.request.LoginRequest;
import com.example.school.auth.application.dto.response.LoginResponse;
import com.example.school.auth.domain.RefreshToken;
import com.example.school.auth.domain.RefreshTokenRepository;
import com.example.school.global.apiPayload.status.ErrorStatus;
import com.example.school.global.exception.ApplicationException;
import com.example.school.member.application.PasswordEncoder;
import com.example.school.member.domain.Member;
import com.example.school.member.domain.MemberRepository;
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
    private final JwtProvider jwtProvider;

    public LoginResponse login(LoginRequest request) {
        Member member = memberRepository.findByEmail(request.email())
                .orElseThrow(() -> new ApplicationException(ErrorStatus.LOGIN_ERROR));

        if (!passwordEncoder.matches(request.password(), member.getPassword())) {
            throw new ApplicationException(ErrorStatus.LOGIN_ERROR);
        }

        String accessToken = jwtProvider.createAccessToken(member);
        String refreshToken = jwtProvider.createRefreshToken(member);
        refreshTokenRepository.save(new RefreshToken(refreshToken, member));

        return new LoginResponse(accessToken, refreshToken);
    }
}
