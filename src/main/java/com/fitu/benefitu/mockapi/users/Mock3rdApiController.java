package com.fitu.benefitu.mockapi.users;

import com.fitu.benefitu.global.response.ApiResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class Mock3rdApiController {

    /**
     * 사용자 정보 조회
     * @return
     */
    @GetMapping("/info")
    public ApiResponse<UserInfoResponse> getUserInfo() {

        // 사용자 기본 정보
        BaseInfo baseInfo = BaseInfo.builder()
                .name("김멋사")
                .schoolName("인천대학교")
                .department("컴퓨터공학부")
                .grade(3)
                .residence("인천 연수구")
                .birthDate(LocalDate.of(2003, 5,14))
                .build();

        // 2. 노션 명세서 규격 100% 매핑 사용자 상세 정보
        DetailInfo detailInfo = DetailInfo.builder()
                .gpa(3.8f)
                .incomeBracket(5)
                .isBasicLiving(false)
                .isSecondLowest(false)
                .interests(Arrays.asList("SCHOLARSHIP", "CAMPUS_WORK", "YOUTH_SUPPORT"))
                .build();

        // 3. 최종 응답 객체 조립
        UserInfoResponse response = new UserInfoResponse(baseInfo, detailInfo);

        return ApiResponse.success(response);
    }


    @Getter
    @RequiredArgsConstructor
    public static class UserInfoResponse {
        private final BaseInfo baseInfo;
        private final DetailInfo detailInfo;
    }

    @Getter
    @Builder
    public static class BaseInfo {
        private String name;
        private String schoolName;
        private String department;
        private Integer grade;
        private String residence;
        private LocalDate birthDate;  // [년]-[월]-[일]
    }

    @Getter
    @Builder
    public static class DetailInfo {
        private Float gpa;
        private Integer incomeBracket;
        private Boolean isBasicLiving;
        private Boolean isSecondLowest;
        private List<String> interests;
    }
}
