package com.umc.ourcampus.member.domain;

import com.umc.ourcampus.university.domain.Department;
import com.umc.ourcampus.university.domain.University;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(Email email);

    Optional<Member> findByEmail_Address(String emailAddress);

    Optional<Member> findByUniversityAndNameAndStudentId(University university, String name, String studentId);

    List<Member> findByDepartment(Department department);
}
