package com.umc.ourcampus.member.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.umc.ourcampus.university.domain.University;
import com.umc.ourcampus.fixture.UniversityFixture;
import com.umc.ourcampus.global.apiPayload.status.ErrorStatus;
import com.umc.ourcampus.global.exception.ApplicationException;
import com.umc.ourcampus.reservation.infrastructure.PasswordEncoderStub;
import com.umc.ourcampus.university.domain.Department;
import org.junit.jupiter.api.Test;

class MemberTest {

    @Test
    void 회원의_비밀번호를_변경한다() {
        University university = UniversityFixture.createUniversity();
        PasswordEncoderStub passwordEncoder = new PasswordEncoderStub();
        Member member = Member.create(
                "member",
                new Email("email@email.com"),
                new Password("testPassword123"),
                "profileImageUrl",
                "20202222",
                university,
                new Department("전공1", university),
                passwordEncoder
        );
        String oldPassword = member.getPassword();

        member.changePassword(new Password("newPassword123"), passwordEncoder);

        assertThat(member.getPassword()).isNotEqualTo(oldPassword);
    }

    @Test
    void 회원의_비밀번호를_검증한다() {
        University university = UniversityFixture.createUniversity();
        PasswordEncoderStub passwordEncoder = new PasswordEncoderStub();
        Member member = Member.create(
                "member",
                new Email("email@email.com"),
                new Password("testPassword123"),
                "profileImageUrl",
                "20202222",
                university,
                new Department("전공1", university),
                passwordEncoder
        );

        assertThatCode(() -> member.verifyPassword("testPassword123", passwordEncoder))
                .doesNotThrowAnyException();
    }

    @Test
    void 잘못된_비밀번호로_검증하면_예외가_발생한다() {
        University university = UniversityFixture.createUniversity();
        PasswordEncoderStub passwordEncoder = new PasswordEncoderStub();
        Member member = Member.create(
                "member",
                new Email("email@email.com"),
                new Password("testPassword123"),
                "profileImageUrl",
                "20202222",
                university,
                new Department("전공1", university),
                passwordEncoder
        );

        assertThatThrownBy(() -> member.verifyPassword("badPassword123", passwordEncoder))
                .isInstanceOf(ApplicationException.class)
                .hasMessage(ErrorStatus.LOGIN_ERROR.getMessage());
    }

    @Test
    void 회원의_프로필이미지를_변경한다() {
        University university = UniversityFixture.createUniversity();
        PasswordEncoderStub passwordEncoder = new PasswordEncoderStub();
        Member member = Member.create(
                "member",
                new Email("email@email.com"),
                new Password("testPassword123"),
                "profileImageUrl",
                "20202222",
                university,
                new Department("전공1", university),
                passwordEncoder
        );
        String oldProfile = member.getProfileImage();

        member.changeProfileImage("newProfileImageUrl");

        assertThat(member.getProfileImage()).isNotEqualTo(oldProfile);
    }
}