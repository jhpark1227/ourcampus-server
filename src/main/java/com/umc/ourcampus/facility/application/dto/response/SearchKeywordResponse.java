package com.umc.ourcampus.facility.application.dto.response;

import com.umc.ourcampus.facility.domain.SearchKeyword;

public record SearchKeywordResponse(
        String keyword
) {
    public static SearchKeywordResponse from(SearchKeyword searchKeyword) {
        return new SearchKeywordResponse(searchKeyword.value());
    }
}
