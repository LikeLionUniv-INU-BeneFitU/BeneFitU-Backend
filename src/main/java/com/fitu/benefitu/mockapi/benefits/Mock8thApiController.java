package com.fitu.benefitu.mockapi.benefits;

import com.fitu.benefitu.global.response.ApiResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/benefits")
public class Mock8thApiController {

    /**
     * (8) 현재 사용자가 신청 완료한 혜택 내역 목록 조회 - 완벽 모킹(Mocking)
     * - GET /api/benefits/applied
     * - 명세서 예시 데이터와 날짜 포맷(yyyy-MM-dd)을 한 치의 오차도 없이 반환합니다.
     */
    @GetMapping("/applied")
    public ApiResponse<AppliedBenefitListResponse> getAppliedBenefits(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(value = "page", defaultValue = "0") int page
    ) {
        // 실제 로직 시: userDetails.getUsername() 기반으로 유저 엔티티를 찾고,
        // 해당 유저가 신청(USER_BENEFITS 또는 APPLIED_BENEFITS)한 내역 테이블을 페이징 조회하여 변환

        // 1. 노션 명세서 예시 데이터 100% 반영한 가짜 신청 내역 생성
        AppliedBenefitInfo applied1 = AppliedBenefitInfo.builder()
                .benefitId(42L)
                .benefitName("아산사회복지재단 성적우수 장학금")
                .appliedDate("2026-07-01") // 명세 포맷 반영
                .build();

        AppliedBenefitInfo applied2 = AppliedBenefitInfo.builder()
                .benefitId(105L)
                .benefitName("서울시 청년 대중교통비 지원금")
                .appliedDate("2026-07-02")
                .build();

        // 2. 최종 결과 바디 응답 조립
        AppliedBenefitListResponse response = new AppliedBenefitListResponse(
                Arrays.asList(applied1, applied2)
        );

        return ApiResponse.success(response);
    }

    @Getter
    @RequiredArgsConstructor
    public static class AppliedBenefitListResponse {
        private final List<AppliedBenefitInfo> appliedBenefits;
    }

    @Getter
    @Builder
    public static class AppliedBenefitInfo {
        private Long benefitId;
        private String benefitName;
        private String appliedDate; // yyyy-MM-dd 형식 스트링
    }
}