package com.umc.ourcampus.facility.presentation;

import com.umc.ourcampus.facility.application.SearchHistoryService;
import com.umc.ourcampus.facility.application.dto.response.SearchKeywordResponse;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SearchHistoryController {

    private static final int MAX_PAGE_SIZE = 20;

    private final SearchHistoryService searchHistoryService;

    @GetMapping("/universities/{universityId}/search/popular")
    public List<SearchKeywordResponse> getPopularSearchHistory(
            @PathVariable("universityId") long universityId,
            @RequestParam(name = "size", defaultValue = "10") @Min(1) @Max(MAX_PAGE_SIZE) int size
    ) {
        return searchHistoryService.findPopularByUniversityId(universityId, size);
    }
}