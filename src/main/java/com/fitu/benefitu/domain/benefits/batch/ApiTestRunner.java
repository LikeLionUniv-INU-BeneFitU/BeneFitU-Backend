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
        var response = youthPolicyClient.fetchPolicies(2, 20);

        if (response != null && response.getResult().getYouthPolicyList() != null) {
            System.out.println("데이터 가져오기 성공! 개수: " + response.getResult().getYouthPolicyList().size());
            System.out.println("전체 개수       : " + response.getResult().getPagging().getTotCount());
            System.out.println("페이지 번호      : " + response.getResult().getPagging().getPageNum());
            System.out.println("페이지 크기      : " + response.getResult().getPagging().getPageSize());
            response.getResult().getYouthPolicyList().forEach(policy -> {
                System.out.println("--------------------------------------------------");
                System.out.println("[정책명]          : " + policy.getPlcyNm());
                System.out.println("[정책 설명]         : " + policy.getPlcyExplnCn());
                System.out.println("[기타 사항]         : " + policy.getEtcMttrCn());
                System.out.println("[신청 URL]        : " + policy.getAplyUrlAddr());
                System.out.println("[신청 코드]         : " + policy.getAplyPrdSeCd());
                System.out.println("[신청 기간]         : " + policy.getAplyYmd());
                System.out.println("[사업 종료일]      : " + policy.getBizPrdEndYmd());
                System.out.println("[정책 제공방법 코드]: " + policy.getPlcyPvsnMthdCd());
                System.out.println("[승인 상태 코드]   : " + policy.getPlcyAprvSttsCd());
                System.out.println("[전공 요건 코드]   : " + policy.getPlcyMajorCd());
                System.out.println("[학력 요건 코드]   : " + policy.getSchoolCd());
                System.out.println("[최소 지원 연령]   : " + policy.getSprtTrgtMinAge());
                System.out.println("[최대 지원 연령]   : " + policy.getSprtTrgtMaxAge());
                System.out.println("[소득 기타 내용]   : " + policy.getEarnEtcCn());      // 차상위 계층 판별 시 중요
                System.out.println("[소득 최소 금액]   : " + policy.getEarnMinAmt());
                System.out.println("[소득 최대 금액]   : " + policy.getEarnMaxAmt());
                System.out.println("[정책 특화 요건 코드]: " + policy.getSbizCd());      // 0014003: 기초생활수급자
                System.out.println("[추가 신청 자격]   : " + policy.getAddAplyQlfcCndCn()); // 차상위 관련 문구 확인 가능
                System.out.println("--------------------------------------------------");
            });
        } else {
            System.out.println("API 응답이 텅 비어있음!");
        }
    }
}
