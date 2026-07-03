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
     * (10) 사용자의 조건에 매칭되는 미신청 혜택의 총 금액 합계 조회 - 완벽 모킹(Mocking)
     * - GET /api/benefits/total-amount
     * - 만약 유저 아이디(username)가 "zero-money"라면 명세서 예외 조항인 0원을 반환하도록 설계했습니다.
     */
    @GetMapping("/total-amount") // 혹은 팀의 명세에 맞춰 /summary/amount 등으로 변경 가능
    public ApiResponse<BenefitTotalAmountResponse> getMatchingTotalAmount(
            @AuthenticationPrincipal String username
    ) {
        // 실제 로직 시: userDetails.getUsername() 기반으로 유저의 상세 조건(학점, 소득분위 등)을 다 긁어온 후,
        // 조건에 맞는 미신청 혜택들의 AMOUNT 컬럼을 SQL SUM() 연산하여 긁어옴.

        // [모킹 케이스] 금액이 없거나 0원인 예외 조항 테스트용 분기
        if ("zero-money".equals(username)) {
            return ApiResponse.success(new BenefitTotalAmountResponse(0L));
        }

        // [정상 흐름] 매칭된 가상 혜택들의 총 금액 합계 (예: 2,400,000 + 1,200,000 + 500,000 = 4,100,000)
        BenefitTotalAmountResponse response = new BenefitTotalAmountResponse(4100000L);

        return ApiResponse.success(response);
    }

    @Getter
    @RequiredArgsConstructor
    public static class BenefitTotalAmountResponse {
        private final Long totalAmount; // ⚠️ 만일 매칭되는 혜택이 없거나 금액이 없는 경우 0 반환 규격 준수
    }
}