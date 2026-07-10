package com.fitu.benefitu.domain.users.controller;

import com.fitu.benefitu.domain.users.dto.BaseInfoDto;
import com.fitu.benefitu.domain.users.dto.UsersInfoSubmitRequest;
import com.fitu.benefitu.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UsersController {

    @PostMapping("/info")
    public ResponseEntity<ApiResponse<UsersInfoSubmitRequest>> submitInfo(
            @RequestBody UsersInfoSubmitRequest usersInfoSubmitRequest) {
        return ResponseEntity.ok(ApiResponse.success(usersInfoSubmitRequest));
    }
}
