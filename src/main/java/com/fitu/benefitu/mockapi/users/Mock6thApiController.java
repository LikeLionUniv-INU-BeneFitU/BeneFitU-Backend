package com.fitu.benefitu.mockapi.users;

import com.fitu.benefitu.global.response.ApiResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class Mock6thApiController {

    @GetMapping("/meta-data")
    public ApiResponse<MetaSetupResponse> getMetaSetupData() {
        // 1. 학교 목록 세팅
        SchoolInfo school1 = SchoolInfo.builder()
                .schoolId(1L)
                .schoolName("멋사대학교")
                .department(Arrays.asList(
                        new DepartmentInfo(101L, "컴퓨터공학과"),
                        new DepartmentInfo(102L, "정보통신학과")
                ))
                .build();

        SchoolInfo school2 = SchoolInfo.builder()
                .schoolId(2L)
                .schoolName("한국대학교")
                .department(Arrays.asList(
                        new DepartmentInfo(201L, "경영학과"),
                        new DepartmentInfo(202L, "경제학과"),
                        new DepartmentInfo(203L, "통계학과")
                ))
                .build();

        SchoolInfo school3 = SchoolInfo.builder()
                .schoolId(3L)
                .schoolName("미래대학교")
                .department(Arrays.asList(
                        new DepartmentInfo(301L, "시각디자인학과"),
                        new DepartmentInfo(302L, "산업디자인학과")
                ))
                .build();

        // 2. 거주지역 목록 세팅
        List<ResidenceInfo> residences = Arrays.asList(
                new ResidenceInfo(1L, "서울시 강남구"),
                new ResidenceInfo(2L, "경기도 수원시"),
                new ResidenceInfo(3L, "인천시 부평구")
        );

        // 3. 최종 결과 바디 조립
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
        private List<DepartmentInfo> department; // 명세에 따라 필드명 'department'로 수정
    }

    @Getter
    @RequiredArgsConstructor
    public static class DepartmentInfo {
        private final Long departmentId;
        private final String departmentName;
    }

    @Getter
    @RequiredArgsConstructor
    public static class ResidenceInfo {
        private final Long residenceId;
        private final String residenceName; // 명세에 따라 필드명 'residenceName'으로 수정
    }
}