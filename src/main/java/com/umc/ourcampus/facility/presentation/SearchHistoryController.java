package com.umc.ourcampus.facility.presentation;

import com.umc.ourcampus.facility.application.SearchHistoryService;
import com.umc.ourcampus.facility.application.dto.response.SearchKeywordResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SearchHistoryController {

    private final SearchHistoryService searchHistoryService;

    @GetMapping("/universities/{universityId}/search/popular")
    public List<SearchKeywordResponse> getPopularSearchHistory(
            @PathVariable("universityId") long universityId,
            @RequestParam(name = "size") int size
    ) {
        return searchHistoryService.findPopularByUniversityId(universityId, size);
    }
}