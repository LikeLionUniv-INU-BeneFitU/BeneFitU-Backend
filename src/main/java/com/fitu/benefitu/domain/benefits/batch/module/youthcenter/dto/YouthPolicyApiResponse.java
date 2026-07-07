package com.fitu.benefitu.domain.benefits.batch.module.youthcenter.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

import java.util.List;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class YouthPolicyApiResponse {
    private Result result;

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Result {
        private List<YouthPolicy> youthPolicyList; // 여기서 리스트를 받아야 함
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class YouthPolicy {
        private String plcyNo;      // 정책번호 (source_id로 사용)
        private String plcyNm;      // 정책명 (benefit_name)
        private String plcyExplnCn; // 정책설명
        private String aplyUrlAddr; // 신청URL (benefit_url)
        private String bizPrdEndYmd;// 사업종료일자 (deadLine 처리용)
        // 필요한 다른 필드들만 추가하세요!
    }
}
