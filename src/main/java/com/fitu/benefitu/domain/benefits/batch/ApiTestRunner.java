package com.fitu.benefitu.domain.benefits.batch;

import com.fitu.benefitu.domain.benefits.batch.service.YouthPolicyClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ApiTestRunner implements CommandLineRunner {

    private final YouthPolicyClient youthPolicyClient;

    public ApiTestRunner(YouthPolicyClient youthPolicyClient) {
        this.youthPolicyClient = youthPolicyClient;
    }

    @Override
    public void run(String... args) throws Exception {
        // 일단 1페이지 5개만 가져와보기
        var response = youthPolicyClient.fetchPolicies(1, 5);

        if (response != null && response.getResult().getYouthPolicyList() != null) {
            System.out.println("데이터 가져오기 성공! 개수: " + response.getResult().getYouthPolicyList().size());
            System.out.println("첫 번째 데이터: " + response.getResult().getYouthPolicyList().get(0).getPlcyNm());
        } else {
            System.out.println("API 응답이 텅 비어있음!");
        }
    }
}
