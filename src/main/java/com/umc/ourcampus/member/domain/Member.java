package com.umc.ourcampus.member.domain;

import com.umc.ourcampus.university.domain.Department;
import com.umc.ourcampus.global.exception.ErrorStatus;
import com.umc.ourcampus.global.domain.BaseEntity;
import com.umc.ourcampus.global.exception.ApplicationException;
import com.umc.ourcampus.university.domain.University;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE member SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private Email email;

    private String password;

    private String profileImage;

    private String studentId;

    @OneToOne(fetch = FetchType.LAZY)
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "university_id")
    private University university;

    public static Member create(String name, Email email, Password password, String profileImage, String studentId,
                                University university, Department department, PasswordEncoder passwordEncoder) {
        Member member = new Member();
        member.name = Objects.requireNonNull(name);
        member.email = Objects.requireNonNull(email);
        member.password = passwordEncoder.encode(password);
        member.profileImage = profileImage;
        member.studentId = Objects.requireNonNull(studentId);
        member.university = Objects.requireNonNull(university);
        member.department = Objects.requireNonNull(department);
        return member;
    }

    public void verifyPassword(String rawPassword, PasswordEncoder passwordEncoder) {
        if (!passwordEncoder.matches(rawPassword, this.password)) {
            throw new ApplicationException(ErrorStatus.LOGIN_ERROR);
        }
    }

    public void changePassword(Password password, PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(password);
    }

    public void changeProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public void withdraw() {
        this.name = "탈퇴한 사용자";
        this.email = new Email("deleted_" + this.id + "@deleted.com");
        this.deletedAt = LocalDateTime.now();
    }
}
