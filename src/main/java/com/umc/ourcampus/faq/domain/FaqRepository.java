package com.umc.ourcampus.faq.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FaqRepository extends JpaRepository<Faq, Long>, FaqRepositoryCustom {
}
