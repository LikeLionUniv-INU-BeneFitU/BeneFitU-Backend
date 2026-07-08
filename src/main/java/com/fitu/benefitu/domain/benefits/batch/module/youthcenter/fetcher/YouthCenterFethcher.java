package com.fitu.benefitu.domain.benefits.batch.module.youthcenter.fetcher;

import com.fitu.benefitu.domain.benefits.batch.module.BenefitsFetcher;
import com.fitu.benefitu.domain.benefits.batch.module.FetchedResults;
import com.fitu.benefitu.domain.benefits.batch.module.youthcenter.dto.YouthPolicyApiResponse;
import com.fitu.benefitu.domain.benefits.types.ExtractorId;
import com.fitu.benefitu.domain.benefits.types.ResidenceType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class YouthCenterFethcher implements BenefitsFetcher {
    private final WebClient webClient;
    private final String apiKey;

    public YouthCenterFethcher(
            WebClient.Builder builder,
            @Value("${api.youth-center.key}") String apiKey) {
        this.webClient = builder.baseUrl("https://www.youthcenter.go.kr").build();
        this.apiKey = apiKey;
    }

    @Override
    public boolean hasNextPage(FetchedResults fetchedResults) {
        // 아무것도 없을 경우
        if (fetchedResults == null) {
            return false;
        }
        // 계산: 현재까지 가져온 데이터 총합
        int totalFetched = fetchedResults.pageNum() * fetchedResults.pageSize();

        // 가져온 데이터가 전체 데이터(totCount)보다 작으면 다음 페이지가 있음
        return totalFetched < fetchedResults.totCount();
    }

    @Override
    public FetchedResults fetchBenefits(ExtractorId extractorId, ResidenceType residenceType, int pageNumber, int pageSize) {
        YouthPolicyApiResponse response = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/go/ythip/getPlcy")
                        .queryParam("apiKeyNm", apiKey.trim())
                        .queryParam("rtnType", "json")
                        .queryParam("pageNum", pageNumber)
                        .queryParam("pageSize", pageSize)
                        .queryParam("pageType", "1") // 1: 목록 모드 추가!
                        .queryParam("zipCd", residenceType.getResidenceCode()) // 만약 여러 개라면 "코드1,코드2" 형식 확인 필요
                        .build())
                .retrieve()
                .bodyToMono(YouthPolicyApiResponse.class)
                .block();
        return response != null ? response.toFetchedResults(extractorId, residenceType) : null;
    }
}
