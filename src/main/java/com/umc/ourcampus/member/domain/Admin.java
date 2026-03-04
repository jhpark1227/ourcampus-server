package com.umc.ourcampus.member.domain;

import com.umc.ourcampus.global.domain.BaseEntity;
import com.umc.ourcampus.global.exception.ApplicationException;
import com.umc.ourcampus.global.exception.ErrorStatus;
import com.umc.ourcampus.university.domain.University;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE admin SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
public class Admin extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String loginId;

    private String name;

    private String password;

    @Enumerated(EnumType.STRING)
    private AdminStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    private University university;

    public static Admin requestRegistration(String name, String loginId, Password password, PasswordEncoder passwordEncoder,
                                            University university) {
        Admin admin = new Admin();
        admin.name = Objects.requireNonNull(name);
        admin.loginId = Objects.requireNonNull(loginId);
        admin.password = passwordEncoder.encode(password);
        admin.status = AdminStatus.PENDING;
        admin.university = university;
        return admin;
    }

    public void approve() {
        this.status = AdminStatus.APPROVED;
    }

    public void reject() {
        this.status = AdminStatus.REJECTED;
    }

    public void verifyPassword(String rawPassword, PasswordEncoder passwordEncoder) {
        if (!passwordEncoder.matches(rawPassword, this.password)) {
            throw new ApplicationException(ErrorStatus.LOGIN_ERROR);
        }
    }

    public void verifyApproved() {
        if (this.status != AdminStatus.APPROVED) {
            throw new ApplicationException(ErrorStatus.ADMIN_NOT_APPROVED);
        }
    }
}