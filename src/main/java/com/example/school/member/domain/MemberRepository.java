package com.example.school.member.domain;

import com.example.school.university.domain.University;
import io.lettuce.core.dynamic.annotation.Param;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    @Query("select s from University s where s.name like concat('%', :keyword, '%')")
    List<University> findSchoolByName(@Param("keyword") String keyword);
}
