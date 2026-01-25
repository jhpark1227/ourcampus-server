package com.umc.ourcampus.member.application;

import com.umc.ourcampus.member.application.dto.request.PasswordChangeRequest;
import com.umc.ourcampus.member.domain.MemberRepository;
import com.umc.ourcampus.auth.domain.RefreshTokenRepository;
import com.umc.ourcampus.auth.domain.VerificationTokenIssuer;
import com.umc.ourcampus.auth.domain.VerificationType;
import com.umc.ourcampus.file.application.FileManager;
import com.umc.ourcampus.global.apiPayload.status.ErrorStatus;
import com.umc.ourcampus.global.exception.ApplicationException;
import com.umc.ourcampus.member.application.dto.request.EmailFindRequest;
import com.umc.ourcampus.member.application.dto.request.PasswordResetRequest;
import com.umc.ourcampus.member.application.dto.request.ProfileImageChangeRequest;
import com.umc.ourcampus.member.application.dto.request.RegisterRequest;
import com.umc.ourcampus.member.application.dto.request.WithDrawRequest;
import com.umc.ourcampus.member.application.dto.response.EmailFindResponse;
import com.umc.ourcampus.member.application.dto.response.MemberInfoResponse;
import com.umc.ourcampus.member.domain.Email;
import com.umc.ourcampus.member.domain.Member;
import com.umc.ourcampus.member.domain.Password;
import com.umc.ourcampus.member.domain.PasswordEncoder;
import com.umc.ourcampus.reservation.domain.ReservationRepository;
import com.umc.ourcampus.university.domain.Department;
import com.umc.ourcampus.university.domain.DepartmentRepository;
import com.umc.ourcampus.university.domain.University;
import com.umc.ourcampus.university.domain.UniversityRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final UniversityRepository universityRepository;
    private final DepartmentRepository departmentRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileManager fileManager;
    private final VerificationTokenIssuer verificationTokenIssuer;
    private final RefreshTokenRepository refreshTokenRepository;
    private final ReservationRepository reservationRepository;

    public void register(RegisterRequest request) {
        Email email = new Email(request.email());
        verificationTokenIssuer.validate(request.verificationToken(), email, VerificationType.REGISTER);
        validateDuplicateEmail(email);
        validateImageUrl(request.profileImageUrl());

        University university = universityRepository.findById(request.universityId())
                .orElseThrow(() -> new ApplicationException(ErrorStatus.UNIVERSITY_NOT_FOUND));
        Department department = departmentRepository.findById(request.departmentId())
                .orElseThrow(() -> new ApplicationException(ErrorStatus.DEPARTMENT_NOT_FOUND));

        Member newMember = Member.create(
                request.name(),
                email,
                new Password(request.password()),
                request.profileImageUrl(),
                request.studentId(),
                university,
                department,
                passwordEncoder
        );
        memberRepository.save(newMember);
    }

    public void withdraw(WithDrawRequest request, long memberId) {
        Member member = findMemberById(memberId);
        member.verifyPassword(request.password(), passwordEncoder);
        member.withdraw();
        reservationRepository.deleteByMember(member);
        refreshTokenRepository.deleteByMember(member);
    }

    public MemberInfoResponse getUserInfo(long memberId) {
        Member member = findMemberById(memberId);

        return MemberInfoResponse.from(member);
    }

    private void validateDuplicateEmail(Email email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        if (member.isPresent()) {
            throw new ApplicationException(ErrorStatus.EMAIL_DUPLICATE);
        }
    }

    private void validateImageUrl(String imageUrl) {
        if (imageUrl == null) {
            return;
        }
        if (!fileManager.exist(imageUrl)) {
            throw new ApplicationException(ErrorStatus.IMAGE_NOT_FOUND);
        }
    }

    public void changePassword(PasswordChangeRequest request, long memberId) {
        Member member = findMemberById(memberId);
        member.verifyPassword(request.oldPassword(), passwordEncoder);
        member.changePassword(new Password(request.newPassword()), passwordEncoder);
    }

    public void changeProfileImage(ProfileImageChangeRequest request, long memberId) {
        Member member = findMemberById(memberId);
        validateImageUrl(request.profileImage());
        member.changeProfileImage(request.profileImage());
    }

    private Member findMemberById(long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new ApplicationException(ErrorStatus.MEMBER_NOT_FOUND));
    }

    public EmailFindResponse findEmail(EmailFindRequest request) {
        University university = universityRepository.findById(request.universityId())
                .orElseThrow(() -> new ApplicationException(ErrorStatus.UNIVERSITY_NOT_FOUND));
        Member member = memberRepository.findByUniversityAndNameAndStudentId(university, request.name(), request.studentId())
                .orElseThrow(() -> new ApplicationException(ErrorStatus.MEMBER_NOT_FOUND));
        return EmailFindResponse.from(member);
    }

    public void resetPassword(PasswordResetRequest request) {
        Member member = memberRepository.findByEmail_Address(request.email())
                .orElseThrow(() -> new ApplicationException(ErrorStatus.MEMBER_NOT_FOUND));
        verificationTokenIssuer.validate(request.verificationToken(), new Email(request.email()), VerificationType.PASSWORD_RESET);
        member.changePassword(new Password(request.password()), passwordEncoder);
    }
}

