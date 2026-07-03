package com.fitu.benefitu.global.config.auth;

import com.fitu.benefitu.global.response.ApiResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
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

    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider; // JWT 토큰 발행용 컴포넌트

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@RequestBody LoginRequest request) {
        // 1. 프론트가 보낸 JSON 데이터를 시큐리티 인증 토큰으로 변환
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());

        // 2. CustomUserDetailsService의 loadUserByUsername이 여기서 실행된다.
        // 만일 에러 발생하면, 여기서 GeneralException이 터져서 전역 핸들러로 감!
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        // 3. 인증 성공 시 JWT 토큰 발행
        // 토큰 만료 기간 : 7 일
        long expiry = 3600L * 24 * 7;
        String accessToken = jwtProvider.createToken(authentication, expiry);

        return ApiResponse.success(new LoginResponse(accessToken));
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