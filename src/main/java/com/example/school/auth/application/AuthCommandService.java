package com.example.school.auth.application;

import com.example.school.auth.config.util.JwtUtils;
import com.example.school.auth.config.util.RedisUtils;
import com.example.school.global.apiPayload.GeneralException;
import com.example.school.global.apiPayload.status.ErrorStatus;
import com.example.school.user.domain.Member;
import com.example.school.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthCommandService {
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final RedisUtils redisUtils;

    public Boolean withdrawUser(String accessToken) {
        String resolveToken = jwtUtils.resolveToken(accessToken);
        String email = jwtUtils.getEmailInToken(resolveToken);
        Member member = userRepository.findByEmail(email).orElseThrow(() -> {
            throw new GeneralException(ErrorStatus.MEMBER_NOT_FOUND);
        });
        userRepository.delete(member);
        logout(accessToken);
        return true;
    }

    public void logout(String accessToken) {
        String resolvedToken = jwtUtils.resolveToken(accessToken);

        String email = jwtUtils.getEmailInToken(resolvedToken);
        String data = redisUtils.getData("RT:" + email);
        if (data != null) {
            redisUtils.deleteData("RT:" + email);
        } else {
            throw new GeneralException(ErrorStatus.REFRESHTOKEN_NOT_FOUND);
        }

        redisUtils.setDataExpire(resolvedToken, "logout", jwtUtils.getExpiration(resolvedToken));
    }

}
