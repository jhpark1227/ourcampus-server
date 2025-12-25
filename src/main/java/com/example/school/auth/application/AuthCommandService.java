package com.example.school.auth.application;

import com.example.school.global.apiPayload.GeneralException;
import com.example.school.global.apiPayload.status.ErrorStatus;
import com.example.school.member.domain.Member;
import com.example.school.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthCommandService {
    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    public Boolean withdrawUser(String accessToken) {
        String resolveToken = jwtProvider.resolveToken(accessToken);
        String email = jwtProvider.getEmailInToken(resolveToken);
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> {
            throw new GeneralException(ErrorStatus.MEMBER_NOT_FOUND);
        });
        memberRepository.delete(member);
        logout(accessToken);
        return true;
    }

    public void logout(String accessToken) {
        String resolvedToken = jwtProvider.resolveToken(accessToken);

        String email = jwtProvider.getEmailInToken(resolvedToken);
    }

}
