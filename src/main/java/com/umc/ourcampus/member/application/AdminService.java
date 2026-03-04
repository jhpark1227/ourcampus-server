package com.umc.ourcampus.member.application;

import com.umc.ourcampus.global.exception.ApplicationException;
import com.umc.ourcampus.global.exception.ErrorStatus;
import com.umc.ourcampus.member.application.dto.request.AdminRegisterRequest;
import com.umc.ourcampus.member.application.dto.response.AdminInfoResponse;
import com.umc.ourcampus.member.domain.Admin;
import com.umc.ourcampus.member.domain.AdminRepository;
import com.umc.ourcampus.member.domain.Password;
import com.umc.ourcampus.member.domain.PasswordEncoder;
import com.umc.ourcampus.university.domain.University;
import com.umc.ourcampus.university.domain.UniversityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;
    private final UniversityRepository universityRepository;
    private final PasswordEncoder passwordEncoder;

    public void register(AdminRegisterRequest request) {
        University university = universityRepository.findById(request.universityId())
                .orElseThrow(() -> new ApplicationException(ErrorStatus.UNIVERSITY_NOT_FOUND));
        if (adminRepository.findByLoginId(request.loginId()).isPresent()) {
            throw new ApplicationException(ErrorStatus.ADMIN_ALREADY_EXIST);
        }
        Admin admin = Admin.requestRegistration(
                request.name(),
                request.loginId(),
                new Password(request.password()),
                passwordEncoder,
                university
        );
        adminRepository.save(admin);
    }

    public AdminInfoResponse getMyInfo(long adminId) {
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new ApplicationException(ErrorStatus.ADMIN_NOT_FOUND));
        return AdminInfoResponse.from(admin);
    }
}
