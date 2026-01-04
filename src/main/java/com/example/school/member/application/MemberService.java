package com.example.school.member.application;

import com.example.school.file.application.FileManager;
import com.example.school.global.apiPayload.status.ErrorStatus;
import com.example.school.global.exception.ApplicationException;
import com.example.school.member.application.dto.request.RegisterRequest;
import com.example.school.member.application.dto.request.VerificationEmailRequest;
import com.example.school.member.application.dto.response.MemberInfoResponse;
import com.example.school.member.domain.EmailVerification;
import com.example.school.member.domain.EmailVerificationRepository;
import com.example.school.member.domain.Member;
import com.example.school.member.domain.MemberRepository;
import com.example.school.member.domain.Password;
import com.example.school.university.domain.University;
import com.example.school.university.domain.UniversityRepository;
import java.util.Optional;
import java.util.random.RandomGeneratorFactory;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final UniversityRepository universityRepository;
    private final EmailVerificationRepository emailVerificationRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileManager fileManager;
    private final EmailSender emailSender;

    public void register(RegisterRequest request) {
        validateDuplicateEmail(request.email());
        validateImageUrl(request.profileImageUrl());
        University university = universityRepository.findById(request.universityId())
                .orElseThrow(() -> new ApplicationException(ErrorStatus.UNIVERSITY_NOT_FOUND));
        Member newMember = Member.builder()
                .name(request.name())
                .email(request.email())
                .password(passwordEncoder.encode(new Password(request.password())))
                .university(university)
                .profileImage(request.profileImageUrl())
                .build();

        memberRepository.save(newMember);
    }

    public MemberInfoResponse getUserInfo(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(ErrorStatus.MEMBER_NOT_FOUND));

        return MemberInfoResponse.from(member);
    }

    public void sendVerificationEmail(VerificationEmailRequest request) {
        String code = createVerificationCode();
        emailSender.sendVerificationEmail(code);
        emailVerificationRepository.save(new EmailVerification(request.email(), code));
    }

    private String createVerificationCode() {
        return RandomGeneratorFactory.getDefault().create()
                .ints(4, 0, 10)
                .mapToObj(String::valueOf)
                .collect(Collectors.joining());
    }

    private void validateDuplicateEmail(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        if (member.isPresent()) {
            throw new ApplicationException(ErrorStatus.EMAIL_DUPLICATE);
        }
    }

    private void validateImageUrl(String imageUrl) {
        if (fileManager.exist(imageUrl)) {
            return;
        }
        throw new ApplicationException(ErrorStatus.IMAGE_NOT_FOUND);
    }
}

