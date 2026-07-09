package com.fitu.benefitu.domain.users.controller;

import com.fitu.benefitu.domain.users.dto.UsersInfoResponse;
import com.fitu.benefitu.domain.users.dto.UsersSubmitInfoRequest;
import com.fitu.benefitu.domain.users.dto.UsersSubmitInfoResponse;
import com.fitu.benefitu.domain.users.service.UsersService;
import com.fitu.benefitu.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UsersController {

    private final UsersService usersService;

    @PostMapping("/info")
    public ResponseEntity<ApiResponse<UsersSubmitInfoResponse>> submitInfo(
            @AuthenticationPrincipal String username,
            @RequestBody UsersSubmitInfoRequest request
    ) {
        UsersSubmitInfoResponse response = usersService.submitInfo(request, username);
        return ResponseEntity.ok(ApiResponse.success(response));

    }

    @GetMapping("/info")
    public ResponseEntity<ApiResponse<UsersInfoResponse>> getUserInfo(
            @AuthenticationPrincipal String username
    ) {

        UsersInfoResponse response = usersService.getUserInfo(username);
        return ResponseEntity.ok(ApiResponse.success(response));
    }


}
