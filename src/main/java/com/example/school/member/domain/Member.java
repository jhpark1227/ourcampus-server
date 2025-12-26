package com.example.school.member.domain;

import com.example.school.global.apiPayload.status.ErrorStatus;
import com.example.school.global.domain.BaseEntity;
import com.example.school.global.exception.ApplicationException;
import com.example.school.member.application.PasswordEncoder;
import com.example.school.university.domain.University;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    private String profileImage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id")
    private University university;

    public Member(Long id, String name, String email, String password, String profileImage, University university) {
        if (!checkEmailFormat(email)) {
            throw new ApplicationException(ErrorStatus.EMAIL_FORMAT_ERROR);
        }
        this.id = id;
        this.university = university;
        this.name = name;
        this.email = email;
        this.password = password;
        this.profileImage = profileImage;
    }

    private Boolean checkEmailFormat(String email) {
        if (!email.matches("^.+@.+$")) {
            return false;
        } else {
            return true;
        }
    }

    public void changePassword(PasswordEncoder passwordEncoder, Password newPassword) {
        this.password = passwordEncoder.encode(newPassword);
    }
}
