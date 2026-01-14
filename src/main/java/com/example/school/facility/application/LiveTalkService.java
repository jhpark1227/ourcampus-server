package com.example.school.facility.application;

import com.example.school.facility.application.dto.request.LiveTalkRequest;
import com.example.school.facility.application.dto.response.LiveTalkResponse;
import com.example.school.facility.domain.Facility;
import com.example.school.facility.domain.FacilityRepository;
import com.example.school.facility.domain.LiveTalk;
import com.example.school.facility.domain.LiveTalkRepository;
import com.example.school.global.apiPayload.status.ErrorStatus;
import com.example.school.global.exception.ApplicationException;
import com.example.school.member.domain.Member;
import com.example.school.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class LiveTalkService {

    private final LiveTalkRepository liveTalkRepository;
    private final FacilityRepository facilityRepository;
    private final MemberRepository memberRepository;

    public long createLiveTalk(long memberId, long facilityId, LiveTalkRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ApplicationException(ErrorStatus.MEMBER_NOT_FOUND));
        Facility facility = facilityRepository.findById(facilityId)
                .orElseThrow(() -> new ApplicationException(ErrorStatus.FACILITY_NOT_FOUND));
        LiveTalk liveTalk = LiveTalk.from(request.message(), facility, member);
        return liveTalkRepository.save(liveTalk).getId();
    }

    public Page<LiveTalkResponse> getFacilityLiveTalk(long facilityId, Pageable pageable) {
        Facility facility = facilityRepository.findById(facilityId)
                .orElseThrow(() -> new ApplicationException(ErrorStatus.FACILITY_NOT_FOUND));
        return liveTalkRepository.findByFacilityOrderByCreatedAtDesc(facility, pageable)
                .map(LiveTalkResponse::from);
    }
}
