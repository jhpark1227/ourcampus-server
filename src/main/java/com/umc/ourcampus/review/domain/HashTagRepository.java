package com.umc.ourcampus.review.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface HashTagRepository extends JpaRepository<HashTag, Long>, HashTagRepositoryCustom {
}
