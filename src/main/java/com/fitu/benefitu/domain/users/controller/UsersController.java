package com.fitu.benefitu.domain.users.controller;

import com.fitu.benefitu.domain.users.dto.UsersInfoSubmitRequest;
import com.fitu.benefitu.domain.users.dto.UsersInfoSubmitResponse;
import com.fitu.benefitu.domain.users.service.UsersService;
import com.fitu.benefitu.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UsersController {
    private final UsersService usersService;

    @PostMapping("/info")
    public ResponseEntity<ApiResponse<UsersInfoSubmitResponse>> submitInfo(
            @RequestBody UsersInfoSubmitRequest usersInfoSubmitRequest) {
        UsersInfoSubmitResponse response = usersService.SubmitInfo(usersInfoSubmitRequest);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/info")
    public ResponseEntity<ApiResponse<UsersInfoSubmitResponse>> getInfo(){
        return ResponseEntity.ok(ApiResponse.success(usersService.getInfo()));
    }

    @PatchMapping("/info")
    public ResponseEntity<ApiResponse<UsersInfoSubmitResponse>> updateInfo(
            @RequestBody UsersInfoSubmitRequest usersInfoSubmitRequest
    ){
        return null;
    }
}
