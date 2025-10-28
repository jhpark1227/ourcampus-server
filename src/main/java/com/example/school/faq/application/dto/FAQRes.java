package com.example.school.faq.application.dto;

import com.example.school.faq.domain.FAQ;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class FAQRes {

    @Getter
    @AllArgsConstructor
    public static class FAQSamples {
        List<FAQSample> list;
        int count;
    }

    @Getter
    @AllArgsConstructor
    public static class FAQSample {
        Long id;
        String title;
    }

    @Getter
    public static class FAQList {
        List<Detail> list;
        Integer listSize;
        Integer totalPage;
        Long totalElements;
        Boolean isFirst;
        Boolean isLast;

        public FAQList(Page<FAQ> entities) {
            list = entities.stream().map(entity -> new Detail(entity))
                    .collect(Collectors.toList());
            listSize = entities.getSize();
            totalPage = entities.getTotalPages();
            totalElements = entities.getTotalElements();
            isFirst = entities.isFirst();
            isLast = entities.isLast();
        }
    }

    @Getter
    public static class Detail {
        String title;
        String content;

        public Detail(FAQ faq) {
            title = faq.getTitle();
            content = faq.getContent();
        }
    }
}
