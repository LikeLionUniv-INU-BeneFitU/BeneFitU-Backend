package com.fitu.benefitu.mockapi.benefits;

import com.fitu.benefitu.global.response.ApiResponse;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/benefits")
public class Mock9thApiController {

    /**
     * (9) 미신청 혜택 카테고리별 개수 집계 조회
     * - 명세서의 카테고리별 카운트 응답 규격 반영
     */
    @GetMapping("/count-by-category")
    public ApiResponse<BenefitCountResponse> getUnappliedBenefitCounts(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        // 명세서에 정의된 카테고리별 필드에 맞춰 가상의 카운트 데이터 생성
        BenefitCountResponse response = BenefitCountResponse.builder()
                .corporateCount(5)
                .regionCount(2)
                .requirementsCount(8)
                .stateCount(11)
                .build();

        return ApiResponse.success(response);
    }

    @Getter
    @Builder
    public static class BenefitCountResponse {
        private int corporateCount;
        private int regionCount;
        private int requirementsCount;
        private int stateCount;
    }
}