package com.fitu.benefitu.mockapi.auth;

import com.fitu.benefitu.global.error.exception.GeneralException;
import com.fitu.benefitu.global.response.ApiResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class MockAuthController {

    @PostMapping("/signup")
    public ApiResponse<SignupResponse> signup(@RequestBody SignupRequest request) {
        if (request.getUsername().equals("already")) {
            throw new GeneralException(AuthErrorCode.DUPLICATE_USERNAME);
        } else if (request.getUsername().equals("wrong-form")) {
            throw new GeneralException(AuthErrorCode.INVALID_PASSWORD_FORMAT);
        }

        SignupResponse response = new SignupResponse(request.getUsername());
        return ApiResponse.success(response);
    }

    @Getter
    public static class SignupRequest {
        private String username;
        private String password;
    }

    @Getter
    @RequiredArgsConstructor
    public static class SignupResponse {
        // 명세서 규격에 맞춘 성공 필드
        private final String username;
    }
}
