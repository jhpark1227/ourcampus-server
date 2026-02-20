package com.umc.ourcampus.member.application;

import com.umc.ourcampus.global.apiPayload.status.ErrorStatus;
import com.umc.ourcampus.global.exception.ApplicationException;
import com.umc.ourcampus.member.application.dto.request.InquireRequest;
import com.umc.ourcampus.member.application.dto.response.InquiryResponse;
import com.umc.ourcampus.member.domain.Inquiry;
import com.umc.ourcampus.member.domain.InquiryRepository;
import com.umc.ourcampus.member.domain.Member;
import com.umc.ourcampus.member.domain.MemberRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class InquiryService {

    private final InquiryRepository inquiryRepository;
    private final MemberRepository memberRepository;

    public long inquire(InquireRequest request, long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ApplicationException(ErrorStatus.MEMBER_NOT_FOUND));
        Inquiry inquiry = Inquiry.create(request.title(), request.content(), member);

        return inquiryRepository.save(inquiry).getId();
    }

    public List<InquiryResponse> getMyInquiries(long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ApplicationException(ErrorStatus.MEMBER_NOT_FOUND));
        return inquiryRepository.findByMemberOrderByCreatedAtDesc(member)
                .stream()
                .map(InquiryResponse::from)
                .toList();
    }
}
