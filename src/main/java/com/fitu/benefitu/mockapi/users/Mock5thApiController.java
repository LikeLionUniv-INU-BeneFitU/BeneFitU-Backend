package com.fitu.benefitu.mockapi.users;

import com.fitu.benefitu.global.response.ApiResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class Mock5thApiController {

    @PatchMapping("/info")
    public ApiResponse<?> updateUserInfo(
            @RequestBody UserInfoRequest request
    ) {
        // [모킹 케이스] 잘못된 상세 정보 입력 (학교명 "wrong-form" 시 400 에러)
        if (request.getBaseInfo() != null && "wrong-form".equals(request.getBaseInfo().getSchoolName())) {
            Map<String, String> errorDetails = new HashMap<>();
            errorDetails.put("schoolName", "존재하지 않는 학교 이름입니다.");
            errorDetails.put("gpa", "학점은 0.0점에서 4.5점 사이여야 합니다.");
            errorDetails.put("incomeBracket", "소득분위는 1에서 10 사이의 정수여야 합니다.");

            return ApiResponse.fail(UsersErrorCode.USER_4002, errorDetails);
        }

        // [정상 흐름] 수정 완료 후 갱신된 데이터를 그대로 반환
        UserInfoResponse response = UserInfoResponse.builder()
                .baseInfo(BaseInfoResponse.builder()
                        .schoolName(request.getBaseInfo().getSchoolName())
                        .department(request.getBaseInfo().getDepartment())
                        .grade(request.getBaseInfo().getGrade())
                        .residence(request.getBaseInfo().getResidence())
                        .birthDate(request.getBaseInfo().getBirthDate()) // birthDate 반영
                        .build())
                .detailInfo(request.getDetailInfo())
                .build();

        return ApiResponse.success(response);
    }

    // --- 요청 객체 ---
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
        private String birthDate; // API 명세 및 요청하신 대로 생년월일(String/날짜포맷) 반영
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

    // --- 응답 객체 ---
    @Getter
    @Builder
    public static class UserInfoResponse {
        private final BaseInfoResponse baseInfo;
        private final RequestDetailInfo detailInfo;
    }

    @Getter
    @Builder
    public static class BaseInfoResponse {
        private String schoolName;
        private String department;
        private Integer grade;
        private String residence;
        private String birthDate; // 응답 데이터에도 birthDate 사용
    }
}