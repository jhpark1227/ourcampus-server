package com.example.school.faq.domain;

import java.util.List;
import org.springframework.data.domain.Pageable;

public interface FaqRepositoryCustom {
    List<Faq> findByType(FaqType type, Pageable page);
}
