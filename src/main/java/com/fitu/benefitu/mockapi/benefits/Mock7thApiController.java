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

    /**
     * (7) 조건별 혜택 목록 필터링/정렬 조회 - 완벽 모킹(Mocking)
     * - @RequestParam에 defaultValue를 주어 미지정 시 기본값(ALL, DEFAULT, 0)이 주입되도록 설계
     */
    @GetMapping
    public ApiResponse<BenefitListResponse> getBenefits(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(value = "category", defaultValue = "ALL") String category,
            @RequestParam(value = "sort", defaultValue = "DEFAULT") String sort,
            @RequestParam(value = "page", defaultValue = "0") int page
    ) {
        // 실제 로직 시: userDetails.getUsername()으로 신청 완료 혜택 테이블을 서브쿼리나 Join 조건(Not In)으로 걷어내고 Dynamic Query(QueryDSL) 가동

        // 1. 전체 가상 혜택 데이터 마스터 셋업
        BenefitInfo item1 = BenefitInfo.builder()
                .benefitId(12L)
                .benefitName("푸른등대 주거안정 장학금")
                .categories(Arrays.asList("SCHOLARSHIP"))
                .dDay("D-5")
                .amount(2400000L)
                .build();

        BenefitInfo item2 = BenefitInfo.builder()
                .benefitId(15L)
                .benefitName("지자체 대학생 교내 행정 근로")
                .categories(Arrays.asList("CAMPUS_WORK", "YOUTH_SUPPORT"))
                .dDay("D-12")
                .amount(1200000L)
                .build();

        BenefitInfo item3 = BenefitInfo.builder()
                .benefitId(18L)
                .benefitName("컴퓨터공학과 SW 경진대회 지원 사업")
                .categories(Arrays.asList("EXTERNAL_ACTIVITY"))
                .dDay("상시모집")
                .amount(500000L)
                .build();

        List<BenefitInfo> allBenefits = Arrays.asList(item1, item2, item3);
        List<BenefitInfo> filteredList = new ArrayList<>();

        // 2. [카테고리 필터 모킹 구동]
        if ("ALL".equalsIgnoreCase(category)) {
            filteredList.addAll(allBenefits);
        } else {
            for (BenefitInfo info : allBenefits) {
                if (info.getCategories().contains(category.toUpperCase())) {
                    filteredList.add(info);
                }
            }
        }

        // 3. [정렬 조건 모킹 구동]
        if ("AMOUNT_HIGH".equalsIgnoreCase(sort)) {
            // 금액 높은 순 정렬 (240만 -> 120만 -> 50만)
            filteredList.sort((a, b) -> b.getAmount().compareTo(a.getAmount()));
        } else if ("DEADLINE_IMMINENT".equalsIgnoreCase(sort)) {
            // 마감 임박순 정렬 (D-5 -> D-12 -> 상시모집)
            // 모킹을 위해 item1, item2, item3 순서 강제 유지
            filteredList.sort((a, b) -> a.getBenefitId().compareTo(b.getBenefitId()));
        }

        // 4. 최종 결과 바디 응답
        return ApiResponse.success(new BenefitListResponse(filteredList));
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
        private Long amount; // 원화 금액
    }
}