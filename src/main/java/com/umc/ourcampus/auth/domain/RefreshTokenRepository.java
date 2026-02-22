package com.umc.ourcampus.auth.domain;

import com.umc.ourcampus.member.domain.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByValueAndMember(String value, Member member);

    void deleteByMember(Member member);

    List<RefreshToken> findByValue(String value);
}
