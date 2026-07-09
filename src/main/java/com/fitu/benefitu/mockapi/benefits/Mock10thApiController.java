package com.fitu.benefitu.mockapi.benefits;

import com.fitu.benefitu.global.response.ApiResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/benefits")
public class Mock10thApiController {

    /**
     * (10) 사용자의 조건에 매칭되는 미신청 혜택의 총 금액 합계 조회
     * - 인증된 사용자의 토큰을 기반으로 매칭되는 혜택 금액 합계 반환
     */
    @GetMapping("/total-amount")
    public ApiResponse<BenefitTotalAmountResponse> getMatchingTotalAmount(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        // 유저 식별용 테스트 분기 (사용자명 "zero-money"인 경우 0원 반환)
        if (userDetails != null && "zero-money".equals(userDetails.getUsername())) {
            return ApiResponse.success(new BenefitTotalAmountResponse(0L));
        }

        // [정상 흐름] 매칭 혜택들의 총 금액 합계 (예시: 4,100,000)
        BenefitTotalAmountResponse response = new BenefitTotalAmountResponse(4100000L);

        return ApiResponse.success(response);
    }

    @Getter
    @RequiredArgsConstructor
    public static class BenefitTotalAmountResponse {
        // 명세서에 명시된 응답 규격(totalAmount) 준수
        private final Long totalAmount;
    }
}