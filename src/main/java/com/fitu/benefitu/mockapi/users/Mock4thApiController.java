package com.fitu.benefitu.mockapi.users;

import com.fitu.benefitu.global.error.exception.GeneralException;
import com.fitu.benefitu.global.response.ApiResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class Mock4thApiController {

    @PostMapping("/info")
    public ApiResponse<?> registerUserInfo(
            @RequestBody UserInfoRequest request
    ) {
        if (request.getBaseInfo() != null && "already".equals(request.getBaseInfo().getSchoolName())) {
            throw new GeneralException(UsersErrorCode.USER_ALREADY_REGISTERED);
        }

        if (request.getBaseInfo() != null && "wrong-form".equals(request.getBaseInfo().getSchoolName())) {
            Map<String, String> errorDetails = new HashMap<>();
            errorDetails.put("schoolName", "존재하지 않는 학교 이름입니다.");
            errorDetails.put("gpa", "학점은 0.0점에서 4.5점 사이여야 합니다.");
            errorDetails.put("incomeBracket", "소득분위는 1에서 10 사이의 정수여야 합니다.");

            return ApiResponse.fail(UsersErrorCode.INVALID_USER_DETAIL, errorDetails);
        }

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
        private String department;
        private Integer grade;
        private String residence;
        private LocalDate birthDate;  // [년]-[월]-[일]
    }

    @Getter
    public static class RequestDetailInfo {
        private Float gpa;
        private Integer incomeBracket;
        private Boolean isBasicLiving;
        private Boolean isSecondLowest;
        private InterestFields interests;
    }

    @Getter
    public static class InterestFields {
        private Boolean corporate;
        private Boolean region;
        private Boolean requirements;
        private Boolean state;
    }

    @Getter
    @RequiredArgsConstructor
    public static class UserInfoResponse {
        private final RequestBaseInfo baseInfo;
        private final RequestDetailInfo detailInfo;
    }
}