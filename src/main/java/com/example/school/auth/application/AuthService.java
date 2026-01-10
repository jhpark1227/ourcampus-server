package com.example.school.auth.application;

import com.example.school.auth.application.dto.request.LoginRequest;
import com.example.school.auth.application.dto.request.LogoutRequest;
import com.example.school.auth.application.dto.response.LoginResponse;
import com.example.school.auth.domain.LoginTokenIssuer;
import com.example.school.auth.domain.RefreshToken;
import com.example.school.auth.domain.RefreshTokenRepository;
import com.example.school.auth.domain.TokenPair;
import com.example.school.global.apiPayload.status.ErrorStatus;
import com.example.school.global.exception.ApplicationException;
import com.example.school.member.domain.Member;
import com.example.school.member.domain.MemberRepository;
import com.example.school.member.domain.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final LoginTokenIssuer loginTokenIssuer;

    public LoginResponse login(LoginRequest request) {
        Member member = memberRepository.findByEmail_Address(request.email())
                .orElseThrow(() -> new ApplicationException(ErrorStatus.LOGIN_ERROR));
        member.verifyPassword(request.password(), passwordEncoder);

        TokenPair tokenPair = loginTokenIssuer.issueLoginTokenPair(member);
        refreshTokenRepository.save(tokenPair.refreshToken());

        return LoginResponse.from(tokenPair);
    }

    public void logout(LogoutRequest request, long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ApplicationException(ErrorStatus.MEMBER_NOT_FOUND));
        RefreshToken refreshToken = refreshTokenRepository.findByValueAndMember(request.refreshToken(), member)
                .orElseThrow(() -> new ApplicationException(ErrorStatus.REFRESHTOKEN_NOT_FOUND));
        refreshTokenRepository.delete(refreshToken);
    }
}
