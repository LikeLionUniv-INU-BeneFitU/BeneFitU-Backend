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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/benefits")
public class Mock8thApiController {

    /**
     * (8) 현재 사용자가 신청 완료한 혜택 내역 목록 조회
     * - applyStatus 파라미터를 통해 신청 현황별 필터링 기능 추가
     */
    @GetMapping("/applied")
    public ApiResponse<AppliedBenefitListResponse> getAppliedBenefits(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "applyStatus", defaultValue = "ALL") String applyStatus
    ) {
        // 1. 샘플 데이터 셋업
        List<AppliedBenefitInfo> allApplied = Arrays.asList(
                AppliedBenefitInfo.builder().benefitId(42L).benefitName("아산사회복지재단 성적우수 장학금").appliedDate("2026-07-01").applyStatus("SELECTED").build(),
                AppliedBenefitInfo.builder().benefitId(105L).benefitName("서울시 청년 대중교통비 지원금").appliedDate("2026-07-02").applyStatus("UNDER_REVIEW").build(),
                AppliedBenefitInfo.builder().benefitId(106L).benefitName("미래 인재 장학금").appliedDate("2026-07-03").applyStatus("NOT_SELECTED").build()
        );

        // 2. 필터링 로직 (ALL이 아니면 해당 status만 필터링)
        List<AppliedBenefitInfo> filteredList;
        if ("ALL".equalsIgnoreCase(applyStatus)) {
            filteredList = allApplied;
        } else {
            filteredList = allApplied.stream()
                    .filter(b -> b.getApplyStatus().equalsIgnoreCase(applyStatus))
                    .collect(Collectors.toList());
        }

        return ApiResponse.success(new AppliedBenefitListResponse(filteredList));
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
        private String appliedDate;
        private String applyStatus; // 명세서 요구사항 반영
    }
}