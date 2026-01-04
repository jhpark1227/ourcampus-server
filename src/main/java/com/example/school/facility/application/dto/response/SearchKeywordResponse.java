package com.example.school.facility.application.dto.response;

import com.example.school.facility.domain.SearchKeyword;

public record SearchKeywordResponse(
        String keyword
) {
    public static SearchKeywordResponse from(SearchKeyword searchKeyword) {
        return new SearchKeywordResponse(searchKeyword.value());
    }
}
