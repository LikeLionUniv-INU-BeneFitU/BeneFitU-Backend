package com.fitu.benefitu.domain.benefits.batch.engine;

import com.fitu.benefitu.domain.benefits.batch.module.FetchedResults;
import com.fitu.benefitu.domain.benefits.batch.module.RawBenefit;
import com.fitu.benefitu.domain.benefits.batch.module.youthcenter.fetcher.YouthCenterFethcher;
import com.fitu.benefitu.domain.benefits.types.ExtractorId;
import com.fitu.benefitu.domain.benefits.types.ResidenceType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FetchEngine {
    private final YouthCenterFethcher youthCenterFethcher;

    public List<RawBenefit> fetchAllRegions() {
        List<RawBenefit> rawBenefits = new ArrayList<>();

        for (ResidenceType region : ResidenceType.values()) {
            try {
                int pageNum = 1;
                int pageSize = 100;

                // 첫 페이지 호출
                FetchedResults results = youthCenterFethcher.fetchBenefits(ExtractorId.YOUTH_CENTER, region, pageNum, pageSize);
                rawBenefits.addAll(results.benefits());

                // 다음 페이지가 있다면 순회
                while (youthCenterFethcher.hasNextPage(results)) {
                    pageNum++; // 페이지 번호 증가
                    results = youthCenterFethcher.fetchBenefits(ExtractorId.YOUTH_CENTER, region, pageNum, pageSize);
                    rawBenefits.addAll(results.benefits()); // 결과 병합
                }
            } catch (Exception e) {
                System.err.println("지역 데이터 패치 실패 [" + region + "]: " + e.getMessage());
            }
        }
        return rawBenefits;
    }
}
