package com.fitu.benefitu.mockapi.benefits;

import com.fitu.benefitu.global.response.ApiResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/benefits")
public class Mock11thApiController {

    @GetMapping("/{benefitId}")
    public ApiResponse<BenefitDetailResponse> getBenefitDetail(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable("benefitId") Long benefitId
    ) {
        // [모킹 센스] ID가 42인 경우 0원 케이스를 테스트하기 위한 분기
        long amountValue = (benefitId == 999L) ? 0L : 4000000L;
        String amountText = (amountValue == 0L) ? "자세한 사항은 사이트 참고 바랍니다" : "최대 " + amountValue + "원";

        // hasDetils 결정 (notes가 비어있으면 false)
        boolean hasDetails = (amountValue != 0L);

        // 1. 상세 정보 구성
        DetailDto detail = DetailDto.builder()
                .benefitId(benefitId)
                .benefitName("아산사회복지재단 성적우수 장학금")
                .categories(Arrays.asList("SCHOLARSHIP", "YOUTH_SUPPORT"))
                .amount(amountText)
                .deadline("2026-07-15")
                .benefitUrl("https://www.kosaf.go.kr")
                .notes(hasDetails ? Arrays.asList(
                        "타 장학금과 중복 수혜 불가능합니다.",
                        "매 학기 종료 후 성적 보고서를 제출해야 합니다.",
                        "서울 소재 4년제 대학 재학생 한정",
                        "공학 계열 전공자 가산점 부여"
                ) : new ArrayList<>())
                .build();

        // 2. 조건 매칭 정보 구성
        MatchedConditionsDto conditions = MatchedConditionsDto.builder()
                .gpa("학점 3.5 이상")
                .incomeBracket("소득분위 6 이하")
                .isBasicLiving("기초생활수급자이")
                .isSecondLowest("차상위계층 아니")
                .build();

        BenefitDetailResponse response = new BenefitDetailResponse(
                hasDetails,
                detail,
                conditions,
                (benefitId >= 100) ? "45%" : "85%"
        );

        return ApiResponse.success(response);
    }

    @Getter
    @RequiredArgsConstructor
    public static class BenefitDetailResponse {
        private final boolean hasDetils; // 명세서 오타(Detils) 반영
        private final DetailDto benefitDetail;
        private final MatchedConditionsDto matchedConditions;
        private final String passProbability;
    }

    @Getter
    @Builder
    public static class DetailDto {
        private Long benefitId;
        private String benefitName;
        private List<String> categories;
        private String amount;
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