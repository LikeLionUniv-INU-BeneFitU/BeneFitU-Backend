package com.fitu.benefitu.domain.users.controller;

import com.fitu.benefitu.domain.users.dto.AuthSignupRequest;
import com.fitu.benefitu.domain.users.dto.AuthSignupResponse;
import com.fitu.benefitu.domain.users.service.UsersService;
import com.fitu.benefitu.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthApiController {
    private final UsersService usersService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<AuthSignupResponse>> signup(
            @RequestBody AuthSignupRequest request
            ) {
        AuthSignupResponse response = usersService.signup(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
