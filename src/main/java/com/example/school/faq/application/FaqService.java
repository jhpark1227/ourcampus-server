package com.example.school.faq.application;

import com.example.school.faq.application.dto.FAQRes;
import com.example.school.faq.domain.Faq;
import com.example.school.faq.domain.FaqRepository;
import com.example.school.faq.domain.FaqType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class FaqService {

    private final FaqRepository faqRepository;

    public FAQRes.FAQList findFaqs(FaqType type, Pageable page) {
        Page<Faq> entities = faqRepository.findByType(type, page);

        return new FAQRes.FAQList(entities);
    }
}
