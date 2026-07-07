package com.fitu.benefitu.domain.benefits.batch.service;

import com.fitu.benefitu.domain.benefits.batch.module.youthcenter.dto.YouthPolicyApiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class YouthPolicyClient {
    private final WebClient webClient;
    private final String apiKey;

    public YouthPolicyClient(
            WebClient.Builder builder,
            @Value("${api.youth-center.key}") String apiKey) {
        this.webClient = builder.baseUrl("https://www.youthcenter.go.kr").build();
        this.apiKey = apiKey;
    }

    public YouthPolicyApiResponse fetchPolicies(int page, int size) {
        System.out.println("DEBUG: 사용중인 API KEY = " + apiKey);
        return webClient.get()
                .uri(uriBuilder -> uriBuilder.path("/go/ythip/getPlcy")
                        .queryParam("apiKeyNm", apiKey.trim())
                        .queryParam("rtnType", "json")
                        .queryParam("pageNum", page)
                        .queryParam("pageSize", size)
                        .build())
                .retrieve()
                .bodyToMono(YouthPolicyApiResponse.class)
                .block(); // 동기식 처리 (배치니까 가능!)
    }
}
