package com.fitu.benefitu.mockapi.benefits;

import com.fitu.benefitu.global.response.ApiResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/benefits")
public class Mock12thApiController {

    /**
     * (12) 특정 혜택 신청 완료 처리 - 완벽 모킹(Mocking)
     * - POST /api/benefits/{benefitId}/apply
     * - PathVariable로 받은 benefitId를 신청 완료 처리하고, 결과로 해당 ID를 그대로 반환합니다.
     */
    @GetMapping("/{benefitId}/apply")
    public ApiResponse<BenefitApplyResponse> applyBenefit(
            @PathVariable("benefitId") Long benefitId
    ) {
        // 실제 로직 시: userDetails.getUsername() 엔티티와 benefitId를 매핑하는
        // 다대다(N:M) 중간 테이블(예: UserBenefit)에 매핑 데이터를 Insert(저장)하는 로직 수행

        // 명세서 성공 응답(200 OK) 규격에 맞춰 요청받은 혜택 ID를 그대로 결과에 담아 반환
        BenefitApplyResponse response = new BenefitApplyResponse(benefitId);

        return ApiResponse.success(response);
    }

    @Getter
    @RequiredArgsConstructor
    public static class BenefitApplyResponse {
        private final Long appliedBenefitId; // 신청 완료 처리된 혜택 ID 규격 준수
    }
}