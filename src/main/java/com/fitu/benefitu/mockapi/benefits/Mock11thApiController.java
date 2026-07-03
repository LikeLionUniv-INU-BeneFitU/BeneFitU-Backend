package com.fitu.benefitu.mockapi.benefits;

import com.fitu.benefitu.global.response.ApiResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/benefits")
public class Mock11thApiController {

    /**
     * (11) 혜택 상세 조회 - 완벽 모킹(Mocking)
     * - GET /api/benefits/{benefitId}
     * - PathVariable로 benefitId를 받아 해당 혜택의 상세 정보, 조건 매칭 키워드, 합격 확률을 반환합니다.
     */
    @GetMapping("/{benefitId}")
    public ApiResponse<BenefitDetailResponse> getBenefitDetail(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable("benefitId") Long benefitId
    ) {
        // 실제 로직 시: userDetails.getUsername()으로 유저의 조건을 가져오고,
        // benefitId로 혜택의 자격 요건을 가져와 비교 분석 후 매칭 키워드 및 합격 확률(Probability) 연산 점수 도출

        // 1. 명세서 예시 규격을 100% 매핑한 상세 정보(benefitDetail) 구성
        DetailDto detail = DetailDto.builder()
                .benefitId(benefitId) // 요청받은 ID 그대로 동적 바인딩
                .benefitName("아산사회복지재단 성적우수 장학금")
                .categories(Arrays.asList("SCHOLARSHIP", "YOUTH_SUPPORT"))
                .amount(4000000L)
                .deadline("2026-07-15") // 명세서 날짜 포맷 반영
                .benefitUrl("https://www.kosaf.go.kr")
                .notes(Arrays.asList(
                        "타 장학금과 중복 수혜 불가능합니다.",
                        "매 학기 종료 후 성적 보고서를 제출해야 합니다.",
                        "서울 소재 4년제 대학 재학생 한정",
                        "공학 계열 전공자 가산점 부여"
                ))
                .build();

        // 2. 명세서 요청 형식에 맞춘 조건 매칭 정보(matchedConditions) 구성
        MatchedConditionsDto conditions = MatchedConditionsDto.builder()
                .gpa("학점 3.5 이상")
                .incomeBracket("소득분위 6 이하")
                .isBasicLiving("기초생활수급자이")
                .isSecondLowest("차상위계층 아니")
                .build();

        // [모킹 센스] ID가 100번 이상인 혜택은 합격 확률을 다르게 줘서 프론트 UI 변경 테스트가 가능하도록 구성
        int probability = (benefitId >= 100) ? 45 : 85;

        // 3. 최종 응답 객체 조립 및 반환
        BenefitDetailResponse response = new BenefitDetailResponse(detail, conditions, probability);
        return ApiResponse.success(response);
    }

    @Getter
    @RequiredArgsConstructor
    public static class BenefitDetailResponse {
        private final DetailDto benefitDetail;
        private final MatchedConditionsDto matchedConditions;
        private final int passProbability; // 합격 가능성 (%)
    }

    @Getter
    @Builder
    public static class DetailDto {
        private Long benefitId;
        private String benefitName;
        private List<String> categories;
        private Long amount;
        private String deadline;
        private String benefitUrl;
        private List<String> notes;
    }

    @Getter
    @Builder
    public static class MatchedConditionsDto {
        private String gpa;
        private String incomeBracket;
        private String isBasicLiving;
        private String isSecondLowest;
    }
}