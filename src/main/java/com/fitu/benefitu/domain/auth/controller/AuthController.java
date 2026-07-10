package com.fitu.benefitu.domain.auth.controller;

import com.fitu.benefitu.domain.auth.service.AuthService;
import com.fitu.benefitu.domain.auth.dto.AuthSignupRequest;
import com.fitu.benefitu.domain.auth.dto.AuthSignupResponse;
import com.fitu.benefitu.domain.users.entity.Users;
import com.fitu.benefitu.domain.users.repository.UsersRepository;
import com.fitu.benefitu.global.config.auth.JwtProvider;
import com.fitu.benefitu.global.response.ApiResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UsersRepository usersRepository;
    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider; // JWT 토큰 발행용 컴포넌트

    // AuthController.java
    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@RequestBody LoginRequest request) {
        // 1. 인증 위임 (검증 및 인증 객체 생성)
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        // 2. 인증된 객체에서 username을 추출 (DB에서 안전하게 다시 조회)
        Users user = usersRepository.findByUsername(authentication.getName());

        // 3. 토큰 발행
        String accessToken = jwtProvider.createToken(authentication, 3600L * 24 * 7, user.getId());

        return ApiResponse.success(new LoginResponse(accessToken));
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<AuthSignupResponse>> signup(
            @RequestBody AuthSignupRequest request
    ) {
        AuthSignupResponse response = authService.signup(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Getter
    public static class LoginRequest {
        private String username;
        private String password;
    }

    @Getter
    @RequiredArgsConstructor
    public static class LoginResponse {
        private final String accessToken;
    }
}