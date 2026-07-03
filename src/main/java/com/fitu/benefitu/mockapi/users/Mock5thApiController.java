package com.fitu.benefitu.mockapi.users;

import com.fitu.benefitu.global.error.exception.GeneralException;
import com.fitu.benefitu.global.response.ApiResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class Mock5thApiController {

    /**
     * (5) 사용자 정보 수정(갱신) - 완벽 모킹(Mocking)
     * - PUT /api/users/info 경로로 요청을 받아 기존 정보를 업데이트하는 형상입니다.
     * - 409 예외 없이, 성공 응답과 400 상세 입력값 오류 검증 분기만 명세대로 깔끔하게 처리합니다.
     */
    @PatchMapping("/info") // 혹은 팀의 갱신 엔드포인트 명세에 맞춰 경로 수정 가능
    public ApiResponse<?> updateUserInfo(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody UserInfoRequest request
    ) {
        // 실제 로직 시: userDetails.getUsername() 기반으로 기존 USERS, USER_DETAILS 테이블 데이터를 Dirty Checking 등으로 수정

        // [모킹 케이스] 잘못된 상세 정보 입력 테스트 (학교명에 "wrong-form" 입력 시 400 및 상세 에러 뱉기)
        if (request.getBaseInfo() != null && "wrong-form".equals(request.getBaseInfo().getSchoolName())) {
            Map<String, String> errorDetails = new HashMap<>();
            errorDetails.put("schoolName", "존재하지 않는 학교 이름입니다.");
            errorDetails.put("gpa", "학점은 0.0점에서 4.5점 사이여야 합니다.");
            errorDetails.put("incomeBracket", "소득분위는 1에서 10 사이의 정수여야 합니다.");

            // 앞서 검증한 팀의 공통 fail 규격 활용
            return ApiResponse.fail(UsersErrorCode.INVALID_USER_DETAIL, errorDetails);
        }

        // [정상 흐름] 수정 완료 후 갱신된 데이터를 그대로 복사해서 200 OK로 반환
        UserInfoResponse response = new UserInfoResponse(
                request.getBaseInfo(),
                request.getDetailInfo()
        );

        return ApiResponse.success(response);
    }

    @Getter
    public static class UserInfoRequest {
        private RequestBaseInfo baseInfo;
        private RequestDetailInfo detailInfo;
    }

    @Getter
    public static class RequestBaseInfo {
        private String schoolName;
        private String majorName;
        private Integer grade;
        private String residence;
    }

    @Getter
    public static class RequestDetailInfo {
        private Float gpa;
        private Integer incomeBracket;
        private Boolean isBasicLiving;
        private Boolean isSecondLowest;
        private List<String> interests;
    }

    @Getter
    @RequiredArgsConstructor
    public static class UserInfoResponse {
        private final RequestBaseInfo baseInfo;
        private final RequestDetailInfo detailInfo;
    }
}