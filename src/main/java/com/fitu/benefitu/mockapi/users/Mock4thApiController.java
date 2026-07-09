package com.fitu.benefitu.mockapi.users;

import com.fitu.benefitu.global.error.exception.GeneralException;
import com.fitu.benefitu.global.response.ApiResponse;
import lombok.Getter;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class Mock4thApiController {

    @PostMapping("/info")
    public ApiResponse<?> registerUserInfo(
            @RequestBody UserInfoRequest request
    ) {
        // 3.3. 최초 설문이 아닌 경우 (409 Conflict)
        if (request.getBaseInfo() != null && "already".equals(request.getBaseInfo().getSchoolName())) {
            throw new GeneralException(UsersErrorCode.USER_409);
        }

        // 3.3. 잘못된 상세 정보 입력 (400 Bad Request)
        if (request.getBaseInfo() != null && "wrong-form".equals(request.getBaseInfo().getSchoolName())) {
            Map<String, String> errorDetails = new HashMap<>();
            errorDetails.put("schoolName", "존재하지 않는 학교 이름입니다.");
            errorDetails.put("gpa", "학점은 0.0점에서 4.5점 사이여야 합니다.");
            errorDetails.put("incomeBracket", "소득분위는 1에서 10 사이의 정수여야 합니다.");

            return ApiResponse.fail(UsersErrorCode.USER_4002, errorDetails);
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
        private LocalDate birthDate;  // 생년월일 (API 문서 규격에 따라 LocalDate 유지)
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
    public static class UserInfoResponse {
        private final RequestBaseInfo baseInfo;
        private final RequestDetailInfo detailInfo;

        public UserInfoResponse(RequestBaseInfo baseInfo, RequestDetailInfo detailInfo) {
            this.baseInfo = baseInfo;
            this.detailInfo = detailInfo;
        }
    }
}