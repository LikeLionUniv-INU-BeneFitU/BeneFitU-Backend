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
                int pageSize = 50;

                // 1. 지역별 호출 전 0.5초 대기
                Thread.sleep(500);

                // 첫 페이지 호출
                FetchedResults results = fetchWithRetry(ExtractorId.YOUTH_CENTER, region, pageNum, pageSize);
                if (results != null) {
                    rawBenefits.addAll(results.benefits());

                    // 다음 페이지 순회
                    while (youthCenterFethcher.hasNextPage(results)) {
                        pageNum++;
                        Thread.sleep(300); // 페이지 간에도 약간의 여유
                        results = fetchWithRetry(ExtractorId.YOUTH_CENTER, region, pageNum, pageSize);
                        if (results == null) break;
                        rawBenefits.addAll(results.benefits());
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // 인터럽트 처리
                System.err.println("배치 작업이 중단되었습니다.");
            } catch (Exception e) {
                System.err.println("지역 데이터 패치 최종 실패 [" + region + "]: " + e.getMessage());
            }
        }
        return rawBenefits;
    }

    // 재시도 로직을 가진 별도 메서드
    private FetchedResults fetchWithRetry(ExtractorId id, ResidenceType region, int page, int size) throws Exception {
        int retryCount = 0;
        while (retryCount < 3) { // 최대 3번 재시도
            try {
                return youthCenterFethcher.fetchBenefits(id, region, page, size);
            } catch (Exception e) {
                retryCount++;
                System.err.println("호출 실패, 재시도 중... [" + retryCount + "] " + region);
                Thread.sleep(1000 * retryCount); // 재시도할수록 대기 시간 증가 (Exponential Backoff)
            }
        }
        throw new Exception("3회 재시도 후에도 실패");
    }
}
