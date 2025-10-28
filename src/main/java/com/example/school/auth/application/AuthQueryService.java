package com.example.school.auth.application;

import com.example.school.auth.config.util.JwtUtils;
import com.example.school.auth.config.util.RedisUtils;
import com.example.school.auth.converter.AuthConverter;
import com.example.school.auth.application.dto.AuthRequestDTO;
import com.example.school.auth.application.dto.AuthResponseDTO;
import com.example.school.facility.domain.School;
import com.example.school.facility.domain.SchoolRepository;
import com.example.school.global.apiPayload.GeneralException;
import com.example.school.global.apiPayload.status.ErrorStatus;
import com.example.school.global.awsS3.AwsS3Service;
import com.example.school.user.domain.Member;
import com.example.school.user.domain.UserRepository;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthQueryService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    private final JwtUtils jwtUtils;
    private final RedisUtils redisUtils;
    private final AwsS3Service awsS3Service;
    private final SchoolRepository schoolRepository;

    //    @Override
//    @Transactional
//    public Member register(AuthRequestDTO.RegisterReqDTO registerReqDTO) {
//
//        Member newMember = AuthConverter.toMember(registerReqDTO);
//        return userRepository.save(newMember);
//    }
    @Transactional
    public Member register(AuthRequestDTO.RegisterReqDTO registerReqDTO, MultipartFile profileImage) {
        // 파일 업로드
        String imageUrl = awsS3Service.uploadSingleFile(profileImage);

        // 회원 정보 생성
        Member newMember = AuthConverter.toMember(registerReqDTO);
        newMember.setProfileImg(imageUrl);

        // 회원 정보 저장
        return userRepository.save(newMember);
    }

    public Member findMemberByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() ->
                new GeneralException(ErrorStatus.MEMBER_NOT_FOUND)
        );
    }

    public Boolean checkUserIdFormat(String userId) {

        if (userId.length() < 4 || userId.length() > 15) {
            return false;
        }

        Optional<Member> member = userRepository.findByUserId(userId);
        if (member != null) {
            return false;
        }

        String regex = "^[a-zA-Z]+[a-zA-Z0-9]$";
        if (!userId.matches(regex)) {
            return false;
        }

        return true;
    }

    public Boolean checkIdentifyNumFormat(String identifyNum) {
        if (identifyNum.length() != 13) {
            return false;
        }
        return true;
    }

    public Boolean checkNicknameDuplicate(String nickname) {
        Optional<Member> user = userRepository.findByNickname(nickname);
        return user != null; // 이미 존재하는 경우 true, 그렇지 않은 경우 false 반환
    }

    public Boolean validateDuplicateEmail(String email) {
        Optional<Member> member = userRepository.findByEmail(email);
        if (!member.isEmpty()) {
            return true; // 존재한다면 true 반환
        } else {
            return false; // 존재하지 않으면 false 반환
        }
    }

    public Boolean validateDuplicateUserId(String userId) {
        Optional<Member> member = userRepository.findByUserId(userId);
        if (!member.isEmpty()) {
            return true; // 존재한다면 true 반환
        } else {
            return false; // 존재하지 않으면 false 반환
        }
    }

    public Boolean checkPassword(String password) {

        // 비밀번호 길이
        if (password.length() < 8 || password.length() > 15) {
            return false;
        }

        // 영문자 대소문자 조합
        if (!password.matches(".*[a-zA-Z].*") || !password.matches(".*\\d.*")) {
            return false;
        }

        // 특수문자 확인
//        String specialChars = "[!@#$%&*]";
//        if (!password.matches(".*" + specialChars + ".*")) {
//            return false;
//        }

        return true;
    }

    public Boolean checkEmailFormat(String email) {
        System.out.print(email);

        if (!email.matches(".+@.*ac\\.kr$")) {
            return false;
        } else {
            return true;
        }
    }

    public AuthResponseDTO.LoginResDTO login(AuthRequestDTO.LoginReqDTO request) {
        Optional<Member> memberOptional = userRepository.findByUserId(request.getUserId());

        // id를 잘못 입력한 경우
        if (memberOptional.isEmpty()) {
            throw new GeneralException(ErrorStatus.USER_ID_ERROR);
        }

        Member member = memberOptional.get();

        // 비밀번호를 잘못 입력한 경우
        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new GeneralException(ErrorStatus.PASSWORD_ERROR);
        }

        String accessToken = jwtUtils.createToken(member.getEmail(), JwtUtils.TOKEN_VALID_TIME);
        String refreshToken = redisUtils.getData("RT:" + member.getEmail());

        if (refreshToken == null) {
            // refreshToken이 존재하지 않는다면 설정해줘야함
            String newRefreshToken = jwtUtils.createToken(member.getEmail(), JwtUtils.REFRESH_TOKEN_VALID_TIME);
            redisUtils.setDataExpire("RT:" + member.getEmail(), newRefreshToken,
                    JwtUtils.REFRESH_TOKEN_VALID_TIME_IN_REDIS);
            refreshToken = newRefreshToken;
        }
        String userid = member.getUserId();

        return AuthResponseDTO.LoginResDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userid(userid)
                .memberId(member.getId())
                .accessTokenExpirationTime(JwtUtils.TOKEN_VALID_TIME)
                .build();
    }

    @Transactional
    public Boolean changePassword(AuthRequestDTO.ChangePasswordReqDTO request) {
        String email = jwtUtils.getEmailInToken(request.getToken());
        Member member = userRepository.findByEmail(email).orElseThrow(() -> {
            throw new GeneralException(ErrorStatus.MEMBER_NOT_FOUND);
        });
        //기존 비밀번호와 일치하는지 확인 후 맞을 시 변경
        if (passwordEncoder.matches(request.getCurrentPassword(), member.getPassword())) {
            member.changePassword(passwordEncoder.encode(request.getChangePassword()));
            userRepository.save(member);
            return true;
        } else {
            return false;
        }
    }

    @Transactional
    public Boolean findPasswd(AuthRequestDTO.FindPwRequest request) {
        Member member = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> {
            throw new GeneralException(ErrorStatus.MEMBER_NOT_FOUND);
        });

        // 유저 아이디와 요청에서 받은 아이디가 일치하는지 확인
        if (!member.getUserId().equals(request.getUserId())) {
            throw new GeneralException(ErrorStatus.USERID_MISMATCH);
        }

        // 인증번호가 일치하는지 확인
        if (!mailService.verifyCertificationCode(request.getEmail(), request.getAuthCode())) {
            throw new GeneralException(ErrorStatus.EMAIL_CODE_ERROR);
        }
//        String encryptedPassword = new BCryptPasswordEncoder().encode(request.getPassword());
        member.changePassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(member);

        if (passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            System.out.println("비밀번호가 성공적으로 변경되었습니다.");
        } else {
            System.out.println("비밀번호 변경 실패");
            return false;
        }

        return true;
    }

    public Boolean checkSchoolFormat(Long id, String name) {
        // 주어진 이름으로 학교를 찾습니다.
        School school = schoolRepository.findByName(name);

        // 학교를 찾지 못한 경우나 학교의 id가 null인 경우 false를 반환합니다.
        if (school == null || school.getId() == null) {
            return false;
        }

        // 학교의 id와 주어진 id, 그리고 학교의 name과 주어진 name을 비교하여 모두 일치하는지 확인합니다.
        if (school.getId().equals(id) && school.getName().equals(name)) {
            return true;
        } else {
            return false;
        }
    }

    public AuthResponseDTO.ReissueRespDto reissue(String refreshToken) {
        String resolvedToken = jwtUtils.resolveToken(refreshToken);
        String email = jwtUtils.getEmailInToken(resolvedToken);
        String savedRefreshToken = redisUtils.getData("RT:" + email);
//        log.info("savedRefreshToken : "+savedRefreshToken);
//        log.info("RefreshToken : "+resolvedToken);
        if (refreshToken.isEmpty() || !resolvedToken.equals(savedRefreshToken)) {
            throw new GeneralException(ErrorStatus.INVALID_REFRESH_TOKEN);
        } else {
            String newAccessToken = jwtUtils.createToken(email, JwtUtils.TOKEN_VALID_TIME);
            String newRefreshToken = jwtUtils.createToken(email, JwtUtils.REFRESH_TOKEN_VALID_TIME);
            redisUtils.setDataExpire("RT:" + email, newRefreshToken, JwtUtils.REFRESH_TOKEN_VALID_TIME_IN_REDIS);
            String getToken = redisUtils.getData("RT:" + email);

            return AuthResponseDTO.ReissueRespDto.builder()
                    .newAccessToken(newAccessToken)
                    .newRefreshToken(newRefreshToken)
                    .accessTokenExpirationTime(JwtUtils.TOKEN_VALID_TIME)
                    .build();
        }
    }

    public List<AuthResponseDTO.SchoolResDTO> searchSchool(String schoolName) {
        // 대학 검색 쿼리 수행
        List<School> schools = userRepository.findSchoolByName(schoolName);

        // 검색된 대학이 없을 경우
        if (schools.isEmpty()) {
            // 또는 다른 처리 로직을 수행하거나 예외를 던질 수 있습니다.
            return Collections.emptyList();
        }

        // 검색된 대학들을 DTO로 매핑
        List<AuthResponseDTO.SchoolResDTO> schoolResDTOs = schools.stream()
                .map(school -> new AuthResponseDTO.SchoolResDTO(school.getId(), school.getName()))
                .collect(Collectors.toList());

        // 최종 결과 반환
        return schoolResDTOs;
    }
}
