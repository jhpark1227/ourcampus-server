package com.umc.ourcampus.member.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InquiryRepository extends JpaRepository<Inquiry, Long> {
    List<Inquiry> findByMemberOrderByCreatedAtDesc(Member member);
}