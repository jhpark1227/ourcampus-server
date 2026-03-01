package com.umc.ourcampus.facility.application;

import com.umc.ourcampus.member.domain.MemberRepository;
import com.umc.ourcampus.facility.application.dto.request.LiveTalkRequest;
import com.umc.ourcampus.facility.application.dto.response.LiveTalkResponse;
import com.umc.ourcampus.facility.domain.Building;
import com.umc.ourcampus.facility.domain.BuildingRepository;
import com.umc.ourcampus.facility.domain.LiveTalk;
import com.umc.ourcampus.facility.domain.LiveTalkRepository;
import com.umc.ourcampus.global.exception.ErrorStatus;
import com.umc.ourcampus.global.exception.ApplicationException;
import com.umc.ourcampus.member.domain.Member;
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
    private final BuildingRepository buildingRepository;
    private final MemberRepository memberRepository;

    public long createLiveTalk(long memberId, long buildingId, LiveTalkRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ApplicationException(ErrorStatus.MEMBER_NOT_FOUND));
        Building building = buildingRepository.findById(buildingId)
                .orElseThrow(() -> new ApplicationException(ErrorStatus.BUILDING_NOT_FOUND));
        LiveTalk liveTalk = LiveTalk.from(request.message(), building, member);
        return liveTalkRepository.save(liveTalk).getId();
    }

    public Page<LiveTalkResponse> getBuildingLiveTalk(long buildingId, Pageable pageable) {
        Building building = buildingRepository.findById(buildingId)
                .orElseThrow(() -> new ApplicationException(ErrorStatus.BUILDING_NOT_FOUND));
        return liveTalkRepository.findByBuildingOrderByCreatedAtDesc(building, pageable)
                .map(LiveTalkResponse::from);
    }
}