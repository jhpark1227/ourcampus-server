package com.umc.ourcampus.fixture;

import com.umc.ourcampus.member.domain.Password;
import com.umc.ourcampus.member.domain.Email;
import com.umc.ourcampus.member.domain.Member;
import com.umc.ourcampus.reservation.infrastructure.PasswordEncoderStub;
import com.umc.ourcampus.university.domain.Department;

public class MemberFixture {

    public static Member create(Department department) {
        return Member.create(
                "홍길동",
                new Email("testEmail@email.com"),
                new Password("testPassword123"),
                "profileImage",
                "20201234",
                department.getUniversity(),
                department,
                new PasswordEncoderStub()
        );
    }
}
