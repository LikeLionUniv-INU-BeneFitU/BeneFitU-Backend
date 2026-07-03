package com.fitu.benefitu.mockapi.users;

import com.fitu.benefitu.global.response.ApiResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class Mock6thApiController {

    /**
     * (6) 가입/설문용 학교, 전공, 지역 리스트 일괄 조회 - 완벽 모킹(Mocking)
     * - GET /api/users/meta-data (또는 팀의 조회 엔드포인트 경로)
     * - 셀렉트 박스 렌더링용 더미 데이터를 명세서 예시와 한 치의 오차도 없이 반환합니다.
     */
    @GetMapping("/meta-data") // 혹은 팀의 명세에 맞춰 /options, /setup 등으로 변경 가능
    public ApiResponse<MetaSetupResponse> getMetaSetupData(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        // 1. 멋사대학교 & 전공 목록 세팅
        SchoolInfo school1 = SchoolInfo.builder()
                .schoolId(1L)
                .schoolName("멋사대학교")
                .majors(Arrays.asList(
                        new MajorInfo(101L, "컴퓨터공학과"),
                        new MajorInfo(102L, "정보통신학과")
                ))
                .build();

        // 2. 한국대학교 & 전공 목록 세팅
        SchoolInfo school2 = SchoolInfo.builder()
                .schoolId(2L)
                .schoolName("한국대학교")
                .majors(Arrays.asList(
                        new MajorInfo(201L, "경영학과"),
                        new MajorInfo(202L, "경제학과"),
                        new MajorInfo(203L, "통계학과")
                ))
                .build();

        // 3. 미래대학교 & 전공 목록 세팅
        SchoolInfo school3 = SchoolInfo.builder()
                .schoolId(3L)
                .schoolName("미래대학교")
                .majors(Arrays.asList(
                        new MajorInfo(301L, "시각디자인학과"),
                        new MajorInfo(302L, "산업디자인학과")
                ))
                .build();

        // 4. 거주지역 목록 세팅
        List<ResidenceInfo> residences = Arrays.asList(
                new ResidenceInfo(1L, "서울시 강남구"),
                new ResidenceInfo(2L, "경기도 수원시"),
                new ResidenceInfo(3L, "인천시 부평구")
        );

        // 5. 최종 결과 바디 조립
        MetaSetupResponse response = new MetaSetupResponse(
                Arrays.asList(school1, school2, school3),
                residences
        );

        return ApiResponse.success(response);
    }

    @Getter
    @RequiredArgsConstructor
    public static class MetaSetupResponse {
        private final List<SchoolInfo> schools;
        private final List<ResidenceInfo> residences;
    }

    @Getter @Builder
    public static class SchoolInfo {
        private Long schoolId;
        private String schoolName;
        private List<MajorInfo> majors;
    }

    @Getter
    @RequiredArgsConstructor
    public static class MajorInfo {
        private final Long majorId;
        private final String majorName;
    }

    @Getter
    @RequiredArgsConstructor
    public static class ResidenceInfo {
        private final Long residenceId;
        private final String regionName;
    }
}
