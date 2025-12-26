package com.example.school.facility.application;

import com.example.school.facility.application.dto.FacilityResponseDTO;
import com.example.school.facility.application.dto.response.FacilityResponse;
import com.example.school.facility.domain.Facility;
import com.example.school.facility.domain.FacilityCategory;
import com.example.school.facility.domain.FacilityRepository;
import com.example.school.global.apiPayload.GeneralException;
import com.example.school.global.apiPayload.status.ErrorStatus;
import com.example.school.global.exception.ApplicationException;
import com.example.school.member.domain.Member;
import com.example.school.member.domain.MemberRepository;
import com.example.school.university.domain.University;
import com.example.school.university.domain.UniversityRepository;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class FacilityQueryService {

    private final FacilityRepository facilityRepository;
    private final MemberRepository memberRepository;
    private final FacilityService facilityService;
    private final UniversityRepository universityRepository;
    private final RedisTemplate redisTemplate;

    public FacilityResponse findFacilityById(Long id) {
        Facility facility = facilityRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(ErrorStatus.FACILITY_NOT_FOUND));

        return FacilityResponse.from(facility);
    }

    public FacilityResponseDTO.ListByKeyword getListByKeyword(Long memberId, String keyword) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        List<FacilityResponseDTO.FacilityInKeyword> list = List.of();

        return new FacilityResponseDTO.ListByKeyword(list, list.size());
    }

    public FacilityResponseDTO.SearchResults searchFacility(Long memberId, String keyword, Integer page) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.MEMBER_NOT_FOUND));

        Pageable pageRequest = PageRequest.of(page - 1, 10);
        Page<Facility> entities = facilityRepository.findByNameLikeAndUniversity(keyword.trim(), member.getUniversity(),
                pageRequest);
        facilityService.saveSearchLog(memberId, member.getUniversity().getId(), keyword);

        return new FacilityResponseDTO.SearchResults(entities);
    }

    public FacilityResponseDTO.SearchLogList getSearchLog(Long memberId) {
        String key = facilityService.searchLogKey(memberId);
        Set<String> set = redisTemplate.opsForZSet().reverseRange(key, 0, 9);
        List<String> list = set.stream().collect(Collectors.toList());

        return new FacilityResponseDTO.SearchLogList(list, list.size());
    }

    public List<FacilityResponse> findFacilityByUniversityAndCategory(Long universityId, FacilityCategory category) {
        University university = universityRepository.findById(universityId)
                .orElseThrow(() -> new ApplicationException(ErrorStatus.UNIVERSITY_NOT_FOUND));
        List<Facility> facilities = facilityRepository.findByUniversityAndCategory(university, category);
        return facilities.stream()
                .map(FacilityResponse::from)
                .toList();
    }
}
