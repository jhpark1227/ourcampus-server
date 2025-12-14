package com.example.school.faq.presentation;

import com.example.school.faq.application.FaqService;
import com.example.school.faq.application.dto.FAQRes;
import com.example.school.faq.domain.FaqType;
import com.example.school.global.apiPayload.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class FaqController {

    private final FaqService FAQService;

    @GetMapping("list")
    public ApiResponse<FAQRes.FAQList> getFaqs(
            @RequestParam(name = "type", required = false) FaqType type,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        FAQRes.FAQList res = FAQService.findFaqs(type, pageable);

        return ApiResponse.onSuccess(res);
    }
}
