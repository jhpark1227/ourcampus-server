package com.example.school.member.application;

import com.example.school.global.apiPayload.status.ErrorStatus;
import com.example.school.global.exception.ApplicationException;
import com.example.school.member.application.dto.MemberRequestDTO;
import com.example.school.member.application.dto.request.RegisterRequest;
import com.example.school.member.converter.UserConverter;
import com.example.school.member.domain.Inquiry;
import com.example.school.member.domain.InquiryRepository;
import com.example.school.member.domain.Member;
import com.example.school.member.domain.MemberRepository;
import com.example.school.university.domain.University;
import com.example.school.university.domain.UniversityRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberCommandService {

    private final MemberRepository memberRepository;
    private final InquiryRepository inquiryRepository;
    private final ProfileImageUploader profileImageUploader;
    private final UniversityRepository universityRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public void register(RegisterRequest request, MultipartFile profileImageFile) throws InterruptedException {
        validateDuplicateEmail(request.email());
        University university = universityRepository.findById(request.universityId())
                .orElseThrow(() -> new ApplicationException(ErrorStatus.UNIVERSITY_NOT_FOUND));
        String profileImage = null;
        if (profileImageFile != null) {
            profileImage = profileImageUploader.uploadFile(profileImageFile);
        }

        Member newMember = Member.builder()
                .name(request.name())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .university(university)
                .profileImage(profileImage)
                .build();

        memberRepository.save(newMember);
    }

    public Member updateProfile(Long memberId, MemberRequestDTO.UpdateProfileDTO request, MultipartFile profileImg) {
        Member user = memberRepository.findById(memberId).orElseThrow(() ->
                new RuntimeException("User not found with id: " + memberId));

        // 기존 프로필 사진 URL
        String existingProfilePictureUrl = user.getProfileImage();

        // 새로운 사진이 들어왔고, 기존 사진이 존재할 시 기존 사진 삭제
        if (existingProfilePictureUrl != null && profileImg != null) {
            String existingFileName = extractFileNameFromUrl(existingProfilePictureUrl);
            profileImageUploader.deleteFile(existingFileName);
        }

        // 새 프로필 사진 업로드

        return memberRepository.save(user);
    }

    // URL에서 파일 이름을 추출하는 메서드
    private String extractFileNameFromUrl(String url) {
        int lastIndexOfSlash = url.lastIndexOf("/");
        return url.substring(lastIndexOfSlash + 1);
    }

    public Inquiry createInquiry(Long memberId, MemberRequestDTO.InquiryDTO request) {

        Inquiry inquiry = UserConverter.toInquiry(request);

        return inquiryRepository.save(inquiry);
    }

    private void validateDuplicateEmail(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        if (member.isPresent()) {
            throw new ApplicationException(ErrorStatus.EMAIL_DUPLICATE);
        }
    }

    private Boolean checkSchoolFormat(Long id, String name) {
        // 주어진 이름으로 학교를 찾습니다.
        University university = universityRepository.findByName(name);

        // 학교를 찾지 못한 경우나 학교의 id가 null인 경우 false를 반환합니다.
        if (university == null || university.getId() == null) {
            return false;
        }

        // 학교의 id와 주어진 id, 그리고 학교의 name과 주어진 name을 비교하여 모두 일치하는지 확인합니다.
        if (university.getId().equals(id) && university.getName().equals(name)) {
            return true;
        } else {
            return false;
        }
    }
}

