package com.fitu.benefitu.domain.benefits.batch.module.youthcenter.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fitu.benefitu.domain.benefits.batch.module.FetchedResults;
import com.fitu.benefitu.domain.benefits.batch.module.RawBenefit;
import com.fitu.benefitu.domain.benefits.types.ExtractorId;
import com.fitu.benefitu.domain.benefits.types.ResidenceType;
import lombok.Getter;

import java.util.List;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class YouthPolicyApiResponse {
    private Result result;

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Result {
        private Pagging pagging;
        private List<YouthPolicy> youthPolicyList; // 여기서 리스트를 받아야 함
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Pagging {
        private Integer totCount;   // 전체 정책 개수
        private Integer pageNum;    // 페이지 수
        private Integer pageSize;   // 페이지 크기
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class YouthPolicy {
        private String plcyNm;      // 정책명 (benefit_name)
        private String plcyExplnCn; // 정책 내용
        private String etcMttrCn;   // 기타 사항 내용
        private String aplyUrlAddr; // 신청URL (benefit_url)
        // 신청 기간
        private String aplyPrdSeCd; // 신청 기간 구분 코드
        private String aplyYmd;     // 신청 기간
        private String bizPrdEndYmd;// 사업종료일자 (deadLine 처리용)

        private String plcyPvsnMthdCd;  // 제공방법 -> 카테고리 활용
        private String plcyAprvSttsCd;  // 정책 승인 -> 무조건 승인한 것만
        // 제약 조건
        private String plcyMajorCd; // 전공 코드
        private String schoolCd;    // 학년(대학 졸업, 대졸 예정, 대학 졸업, 제한없음 따짐)
        private String sprtTrgtMinAge;  // 최소 지원 연령
        private String sprtTrgtMaxAge;  // 최대 지원 연령

        // 점수 가중치 목록
        // 소득 분위
        private String earnEtcCn;   // 차상위도 찾는데 활용
        private String earnMinAmt;
        private String earnMaxAmt;
        // 기초 생활 수급자 -> false라도 기초 생활 수급자는 접근 가능
        private String sbizCd;  // [0014003]인 경우만
        // 차상위계층
        private String addAplyQlfcCndCn;    // 추가 신청 자격 조건 내용
        private String ptcpPrpTrgtCn;    // 참여 제한 대상 내용

    }

    public FetchedResults toFetchedResults(ExtractorId extractorId, ResidenceType residenceType) {
        // 1. 방어 코드: result나 pagging이 null일 경우 고려
        if (this.result == null) {
            return new FetchedResults(extractorId, 0, 0, 0, List.of());
        }

        Pagging paging = this.result.getPagging();
        List<YouthPolicy> youthPolicyList = this.result.getYouthPolicyList();

        // 2. 리스트 null 체크 추가 및 필터링 적용
        List<RawBenefit> benefits = (youthPolicyList == null) ? List.of() : youthPolicyList.stream()
                // 승인된 정책만 필터링
                .filter(dto -> "0044002".equals(dto.getPlcyAprvSttsCd()))
                .map(dto -> RawBenefit.from(dto, residenceType))
                .toList();

        // 3. 페이징 객체 null 체크 (안전한 값 반환)
        int totCount = (paging != null && paging.getTotCount() != null) ? paging.getTotCount() : 0;
        int pageNum = (paging != null && paging.getPageNum() != null) ? paging.getPageNum() : 0;
        int pageSize = (paging != null && paging.getPageSize() != null) ? paging.getPageSize() : 0;

        return new FetchedResults(
                extractorId,
                totCount,
                pageNum,
                pageSize,
                benefits
        );
    }
}
