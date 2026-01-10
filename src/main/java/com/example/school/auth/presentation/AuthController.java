package com.example.school.auth.presentation;

import com.example.school.auth.application.AuthService;
import com.example.school.auth.application.dto.request.LoginRequest;
import com.example.school.auth.application.dto.request.LogoutRequest;
import com.example.school.auth.application.dto.response.LoginResponse;
import com.example.school.auth.domain.MemberPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auth/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/auth/logout")
    public ResponseEntity<Void> logout(
            @AuthenticationPrincipal MemberPrincipal memberPrincipal,
            @RequestBody LogoutRequest request
    ) {
        authService.logout(request, memberPrincipal.memberId());
        return ResponseEntity.noContent().build();
    }
}
