package com.umc.ourcampus.facility.presentation;

import com.umc.ourcampus.auth.domain.UserPrincipal;
import com.umc.ourcampus.facility.application.SearchHistoryService;
import com.umc.ourcampus.facility.application.dto.request.SearchHistoryRequest;
import com.umc.ourcampus.facility.application.dto.response.SearchKeywordResponse;
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
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        searchHistoryService.saveSearchHistory(request, userPrincipal.memberId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me/search")
    public List<SearchKeywordResponse> getMySearchHistory(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return searchHistoryService.findSearchHistoryByMemberId(userPrincipal.memberId());
    }

    @GetMapping("/universities/{universityId}/search/popular")
    public List<SearchKeywordResponse> getPopularSearchHistory(
            @RequestParam(name = "size") int size,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        return searchHistoryService.findPopularByUniversityId(userPrincipal.universityId(), size);
    }

    @DeleteMapping("/me/search")
    public void removeMySearchHistory(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam("keyword") String keyword
    ) {
        searchHistoryService.deleteByMemberIdAndKeyword(userPrincipal.memberId(), keyword);
        ResponseEntity.noContent().build();
    }
}
