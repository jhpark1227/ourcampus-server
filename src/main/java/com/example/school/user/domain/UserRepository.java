package com.example.school.user.domain;

import com.example.school.facility.domain.School;
import io.lettuce.core.dynamic.annotation.Param;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);

    Optional<Member> findByUserId(String userId);

    Optional<Member> findByNickname(String nickname);

    @Query("select s from School s where s.name like concat('%', :keyword, '%')")
    List<School> findSchoolByName(@Param("keyword") String keyword);
}
