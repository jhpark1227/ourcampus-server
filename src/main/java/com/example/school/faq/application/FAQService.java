package com.example.school.faq.application;

import com.example.school.faq.domain.FAQ;
import com.example.school.faq.application.dto.FAQRes;
import com.example.school.faq.domain.FAQRepository;
import com.example.school.user.domain.FaqType;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FAQService {
    private final FAQRepository faqRepository;

    public FAQRes.FAQSamples getFAQSamples() {
        List<FAQ> entities = faqRepository.findTop4By();

        List<FAQRes.FAQSample> list = entities.stream().map(entity -> {
            return new FAQRes.FAQSample(entity.getId(), entity.getTitle());
        }).collect(Collectors.toList());

        return new FAQRes.FAQSamples(list, list.size());
    }

    public FAQRes.FAQList getList(String type, Integer page) {
        FaqType faqType = FaqType.valueOf(type.toUpperCase());
        Pageable pageRequest = PageRequest.of(page - 1, 15);

        Page<FAQ> entities = faqRepository.findByType(faqType, pageRequest);

        return new FAQRes.FAQList(entities);
    }
}
