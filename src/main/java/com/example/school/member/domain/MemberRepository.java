package com.example.school.member.domain;

import com.example.school.university.domain.University;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(Email email);

    Optional<Member> findByEmail_Address(String emailAddress);

    Optional<Member> findByUniversityAndNameAndStudentId(University university, String name, String studentId);
}
