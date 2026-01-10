package com.example.school.auth.domain;

import com.example.school.member.domain.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByValueAndMember(String value, Member member);

    void deleteByMember(Member member);
}
