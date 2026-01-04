package com.example.school.facility.application;

import com.example.school.facility.application.dto.request.SearchHistoryRequest;
import com.example.school.facility.application.dto.response.SearchKeywordResponse;
import com.example.school.facility.domain.SearchHistory;
import com.example.school.facility.domain.SearchHistoryRepository;
import com.example.school.facility.domain.SearchKeyword;
import com.example.school.global.apiPayload.status.ErrorStatus;
import com.example.school.global.exception.ApplicationException;
import com.example.school.member.domain.Member;
import com.example.school.member.domain.MemberRepository;
import com.example.school.university.domain.University;
import com.example.school.university.domain.UniversityRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class SearchHistoryService {

    private final MemberRepository memberRepository;
    private final SearchHistoryRepository searchHistoryRepository;
    private final UniversityRepository universityRepository;

    public void saveSearchHistory(SearchHistoryRequest request, long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ApplicationException(ErrorStatus.MEMBER_NOT_FOUND));
        SearchKeyword searchKeyword = new SearchKeyword(request.keyword());
        SearchHistory searchHistory = new SearchHistory(searchKeyword, member);
        searchHistoryRepository.save(searchHistory);
    }

    public List<SearchKeywordResponse> findSearchHistoryByMemberId(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ApplicationException(ErrorStatus.MEMBER_NOT_FOUND));
        return searchHistoryRepository.findKeywordByMemberOrderByLastest(member, PageRequest.of(0, 5));
    }

    public void deleteByMemberIdAndKeyword(long memberId, String keyword) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ApplicationException(ErrorStatus.MEMBER_NOT_FOUND));
        SearchKeyword searchKeyword = new SearchKeyword(keyword);
        searchHistoryRepository.deleteByMemberAndKeyword(member, searchKeyword);
    }

    public List<SearchKeywordResponse> findPopularByUniversityId(long universityId, int size) {
        University university = universityRepository.findById(universityId)
                .orElseThrow(() -> new ApplicationException(ErrorStatus.UNIVERSITY_NOT_FOUND));
        return searchHistoryRepository.findPopularKeywordByUniversity(university, size)
                .stream()
                .map(SearchKeywordResponse::from)
                .toList();
    }
}
