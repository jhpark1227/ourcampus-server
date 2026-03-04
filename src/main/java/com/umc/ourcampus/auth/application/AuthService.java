package com.umc.ourcampus.auth.application;

import com.umc.ourcampus.auth.application.dto.request.AccessTokenReissueRequest;
import com.umc.ourcampus.auth.application.dto.request.AdminLoginRequest;
import com.umc.ourcampus.auth.application.dto.request.LoginRequest;
import com.umc.ourcampus.auth.application.dto.request.LogoutRequest;
import com.umc.ourcampus.auth.application.dto.response.AccessTokenReissueResponse;
import com.umc.ourcampus.auth.application.dto.response.AdminLoginResponse;
import com.umc.ourcampus.auth.application.dto.response.LoginResponse;
import com.umc.ourcampus.auth.domain.LoginTokenIssuer;
import com.umc.ourcampus.auth.domain.RefreshToken;
import com.umc.ourcampus.auth.domain.RefreshTokenRepository;
import com.umc.ourcampus.auth.domain.TokenPair;
import com.umc.ourcampus.global.exception.ApplicationException;
import com.umc.ourcampus.global.exception.ErrorStatus;
import com.umc.ourcampus.member.domain.Admin;
import com.umc.ourcampus.member.domain.AdminRepository;
import com.umc.ourcampus.member.domain.Member;
import com.umc.ourcampus.member.domain.MemberRepository;
import com.umc.ourcampus.member.domain.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final MemberRepository memberRepository;
    private final AdminRepository adminRepository;
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

    public AdminLoginResponse adminLogin(AdminLoginRequest request) {
        Admin admin = adminRepository.findByLoginId(request.loginId())
                .orElseThrow(() -> new ApplicationException(ErrorStatus.LOGIN_ERROR));
        admin.verifyPassword(request.password(), passwordEncoder);
        admin.verifyApproved();
        String accessToken = loginTokenIssuer.issueAdminToken(admin);
        return new AdminLoginResponse(accessToken);
    }

    public void logout(LogoutRequest request, long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ApplicationException(ErrorStatus.MEMBER_NOT_FOUND));
        RefreshToken refreshToken = refreshTokenRepository.findByValueAndMember(request.refreshToken(), member)
                .orElseThrow(() -> new ApplicationException(ErrorStatus.REFRESHTOKEN_NOT_FOUND));
        refreshTokenRepository.delete(refreshToken);
    }

    public AccessTokenReissueResponse refreshAccessToken(AccessTokenReissueRequest request) {
        loginTokenIssuer.validate(request.refreshToken());
        RefreshToken oldRefreshToken = refreshTokenRepository.findByValue(request.refreshToken())
                .stream().findFirst()
                .orElseThrow(() -> new ApplicationException(ErrorStatus.REFRESHTOKEN_NOT_FOUND));
        refreshTokenRepository.delete(oldRefreshToken);

        TokenPair newTokenPair = loginTokenIssuer.issueLoginTokenPair(oldRefreshToken.getMember());
        refreshTokenRepository.save(newTokenPair.refreshToken());

        return AccessTokenReissueResponse.from(newTokenPair);
    }
}
