package com.example.school.member.application;

import com.example.school.global.apiPayload.status.ErrorStatus;
import com.example.school.global.exception.ApplicationException;
import com.example.school.member.application.dto.response.MemberInfoResponse;
import com.example.school.member.domain.Member;
import com.example.school.member.domain.MemberRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberQueryService {

    private final MemberRepository memberRepository;

    public Optional<Member> findMemberById(Long id) {
        return memberRepository.findById(id);
    }

    public MemberInfoResponse getUserInfo(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(ErrorStatus.MEMBER_NOT_FOUND));

        return MemberInfoResponse.from(member);
    }
}
