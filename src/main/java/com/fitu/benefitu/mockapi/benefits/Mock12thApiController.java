package com.fitu.benefitu.mockapi.benefits;

import com.fitu.benefitu.global.response.ApiResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/benefits")
public class Mock12thApiController {

    /**
     * (12) 특정 혜택 신청 상태 변경 처리
     * - POST /api/benefits/{benefitId}/apply
     * - PathVariable로 benefitId, RequestBody로 applyStatus를 받아 상태 변경 처리
     */
    @PostMapping("/{benefitId}/apply")
    public ApiResponse<BenefitApplyResponse> applyBenefit(
            @PathVariable("benefitId") Long benefitId,
            @RequestBody ApplyRequest request
    ) {
        // 실제 로직 시: 요청받은 applyStatus(UNDER_REVIEW, SELECTED, NOT_SELECTED 등)를
        // 유저와 혜택 매핑 테이블에 업데이트하는 로직 수행

        // 명세서 규격에 맞춰 처리된 혜택 ID 반환
        BenefitApplyResponse response = new BenefitApplyResponse(benefitId);

        return ApiResponse.success(response);
    }

    // --- 요청 객체 ---
    @Getter
    public static class ApplyRequest {
        private String applyStatus; // 신청 상태 (UNDER_REVIEW, SELECTED, NOT_SELECTED)
    }

    // --- 응답 객체 ---
    @Getter
    @RequiredArgsConstructor
    public static class BenefitApplyResponse {
        private final Long appliedBenefitId;
    }
}