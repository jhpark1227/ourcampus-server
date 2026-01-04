package com.example.school.facility.presentation;

import com.example.school.auth.domain.MemberPrincipal;
import com.example.school.facility.application.SearchHistoryService;
import com.example.school.facility.application.dto.request.SearchHistoryRequest;
import com.example.school.facility.application.dto.response.SearchKeywordResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SearchHistoryController {

    private final SearchHistoryService searchHistoryService;

    @PostMapping("/me/search")
    public ResponseEntity<Void> saveSearchLog(
            @RequestBody SearchHistoryRequest request,
            @AuthenticationPrincipal MemberPrincipal memberPrincipal
    ) {
        searchHistoryService.saveSearchHistory(request, memberPrincipal.memberId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me/search")
    public List<SearchKeywordResponse> getMySearchHistory(@AuthenticationPrincipal MemberPrincipal memberPrincipal) {
        return searchHistoryService.findSearchHistoryByMemberId(memberPrincipal.memberId());
    }

    @GetMapping("/universities/{universityId}/search/popular")
    public List<SearchKeywordResponse> getPopularSearchHistory(
            @RequestParam(name = "size") int size,
            @AuthenticationPrincipal MemberPrincipal memberPrincipal
    ) {
        return searchHistoryService.findPopularByUniversityId(memberPrincipal.universityId(), size);
    }

    @DeleteMapping("/me/search")
    public void removeMySearchHistory(
            @AuthenticationPrincipal MemberPrincipal memberPrincipal,
            @RequestParam("keyword") String keyword
    ) {
        searchHistoryService.deleteByMemberIdAndKeyword(memberPrincipal.memberId(), keyword);
        ResponseEntity.noContent().build();
    }
}
