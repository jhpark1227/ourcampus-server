package com.umc.ourcampus.faq.presentation;

import com.umc.ourcampus.faq.application.FaqService;
import com.umc.ourcampus.faq.application.dto.response.FaqResponse;
import com.umc.ourcampus.faq.domain.FaqType;
import com.umc.ourcampus.faq.presentation.response.FaqTypeResponse;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class FaqController {

    private final FaqService faqService;

    @GetMapping("/faqs")
    public List<FaqResponse> getFaqs(
            @RequestParam(name = "type", required = false) FaqType type,
            @PageableDefault Pageable page
    ) {
        return faqService.findFaqs(type, page);
    }

    @GetMapping("/faqs/types")
    public List<FaqTypeResponse> getTypes() {
        return Arrays.stream(FaqType.values())
                .map(FaqTypeResponse::from)
                .toList();
    }
}
