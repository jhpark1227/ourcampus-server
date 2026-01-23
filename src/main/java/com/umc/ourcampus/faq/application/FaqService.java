package com.umc.ourcampus.faq.application;

import com.umc.ourcampus.faq.domain.FaqType;
import com.umc.ourcampus.faq.application.dto.response.FaqResponse;
import com.umc.ourcampus.faq.domain.FaqRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class FaqService {

    private final FaqRepository faqRepository;

    public List<FaqResponse> findFaqs(FaqType type, Pageable pageable) {
        return faqRepository.findByType(type, pageable)
                .stream()
                .map(FaqResponse::from)
                .toList();
    }
}
