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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/benefits")
public class Mock7thApiController {

    @GetMapping
    public ApiResponse<BenefitListResponse> getBenefits(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(value = "category", defaultValue = "ALL") String category,
            @RequestParam(value = "sort", defaultValue = "DEFAULT") String sort,
            @RequestParam(value = "page", defaultValue = "0") int page
    ) {
        // 1. 데이터 셋업 (문서 예시에 맞춰 amount를 String으로 구성)
        List<BenefitInfo> allBenefits = Arrays.asList(
                BenefitInfo.builder().benefitId(12L).benefitName("푸른등대 주거안정 장학금").categories(Arrays.asList("STATE")).dDay("D-5").amount("최대 2400000원").build(),
                BenefitInfo.builder().benefitId(15L).benefitName("지자체 대학생 교내 행정 근로").categories(Arrays.asList("REGION", "REQUIREMENTS")).dDay("D-12").amount("최대 1200000원").build(),
                BenefitInfo.builder().benefitId(18L).benefitName("컴퓨터공학과 SW 경진대회 지원 사업").categories(Arrays.asList("CORPORATE")).dDay("상시모집").amount("최대 500000원").build()
        );

        List<BenefitInfo> filteredList = new ArrayList<>();

        // 2. 카테고리 필터링
        if ("ALL".equalsIgnoreCase(category)) {
            filteredList.addAll(allBenefits);
        } else {
            for (BenefitInfo info : allBenefits) {
                if (info.getCategories().contains(category.toUpperCase())) {
                    filteredList.add(info);
                }
            }
        }

        // 3. 정렬 조건 적용 (숫자 추출을 위해 amount 문자열에서 숫자만 파싱)
        if ("AMOUNT_HIGH".equalsIgnoreCase(sort)) {
            filteredList.sort((a, b) -> extractAmount(b.getAmount()).compareTo(extractAmount(a.getAmount())));
        } else if ("DEADLINE_IMMINENT".equalsIgnoreCase(sort)) {
            filteredList.sort((a, b) -> {
                if (a.getDDay().equals("상시모집")) return 1;
                if (b.getDDay().equals("상시모집")) return -1;
                return a.getDDay().compareTo(b.getDDay());
            });
        }

        return ApiResponse.success(new BenefitListResponse(filteredList));
    }

    // 문자열에서 숫자만 추출하는 헬퍼 메서드
    private Long extractAmount(String amountStr) {
        return Long.parseLong(amountStr.replaceAll("[^0-9]", ""));
    }

    @Getter
    @RequiredArgsConstructor
    public static class BenefitListResponse {
        private final List<BenefitInfo> benefits;
    }

    @Getter
    @Builder
    public static class BenefitInfo {
        private Long benefitId;
        private String benefitName;
        private List<String> categories;
        private String dDay;
        private String amount; // API 명세서에 따라 String 타입으로 변경
    }
}