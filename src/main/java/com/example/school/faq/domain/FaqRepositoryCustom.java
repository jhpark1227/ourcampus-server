package com.example.school.faq.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FaqRepositoryCustom {
    Page<Faq> findByType(FaqType type, Pageable page);
}
