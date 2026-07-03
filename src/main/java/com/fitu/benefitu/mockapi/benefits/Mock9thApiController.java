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
     * (9) 미신청 혜택 카테고리별 개수 집계 조회 - 완벽 모킹(Mocking)
     * - GET /api/benefits/count
     * - 사용자가 아직 신청하지 않은 혜택들의 개수를 카테고리별로 집계하여 명세서 규격대로 반환합니다.
     */
    @GetMapping("/count-by-category") // 혹은 팀의 명세에 맞춰 /summary, /dashboard/count 등으로 변경 가능
    public ApiResponse<BenefitCountResponse> getUnappliedBenefitCounts(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        // 실제 로직 시: userDetails.getUsername() 기반으로 유저가 신청한 혜택 ID 목록을 가져온 뒤,
        // 전체 혜택 중 해당 ID들을 제외(NOT IN)하고 카테고리별로 GROUP BY COUNT 쿼리를 날려 집계

        // 노션 명세서 규격을 100% 충족하는 더미 카운트 결과 생성
        BenefitCountResponse response = BenefitCountResponse.builder()
                .scholarshipCount(5)       // 장학금 5개 남음
                .campusWorkCount(2)        // 교내 근로 2개 남음
                .externalActivityCount(8)  // 대외 활동 8개 남음
                .youthSupportCount(11)     // 청년 지원금 11개 남음
                .build();

        return ApiResponse.success(response);
    }

    @Getter
    @Builder
    public static class BenefitCountResponse {
        private int scholarshipCount;
        private int campusWorkCount;
        private int externalActivityCount;
        private int youthSupportCount;
    }
}