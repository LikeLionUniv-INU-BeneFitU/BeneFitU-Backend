package com.fitu.benefitu.domain.benefits.batch;

import com.fitu.benefitu.domain.benefits.batch.engine.FetchEngine;
import com.fitu.benefitu.domain.benefits.batch.module.RawBenefit;
import com.fitu.benefitu.domain.benefits.batch.service.YouthPolicyClient;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ApiTestRunner implements CommandLineRunner {

    private final FetchEngine fetchEngine;

    public ApiTestRunner(FetchEngine fetchEngine) {
        this.fetchEngine = fetchEngine;
    }

    @Override
    public void run(String... args) throws Exception {
        var response1 = fetchEngine.fetchAllRegions();

        if(response1!=null && response1.size()>0) {
            System.out.println("데이터 가져오기 성공! 개수: " + response1.size());
            for(var r : response1) {
                System.out.println("--------------------------------------------------");
                System.out.println("[정책명]          : " + r.BenefitName());
                System.out.println("[정책 설명]         : " + r.contents());
                System.out.println("[신청 URL]        : " + r.BenefitUrl());
                System.out.println("[신청 기간 유뮤]         : " + r.hasDeadLine());
                System.out.println("[신청 기간]         : " + r.deadLine());
                System.out.println("[정책 제공방법]: " + r.applyWay());
                System.out.println("[학교]: " + r.schoolType());
                System.out.println("[전공 요건 코드]   : " + r.departmentType());
                System.out.println("[학력 요건 코드]   : " + r.grade());
                System.out.println("[최소 지원 연령]   : " + r.minAge());
                System.out.println("[최대 지원 연령]   : " + r.maxAge());
                System.out.println("[거주 지역]   : " + r.residence());
                System.out.println("[학점]   : " + r.gpa());
                System.out.println("[소득 최소 금액]   : " + r.minAmt());
                System.out.println("[소득 최대 금액]   : " + r.maxAmt());
                System.out.println("[기초 생활 수급자 여부]: " + r.isBasicLiving());      // 0014003: 기초생활수급자
                System.out.println("[기타 사항]   : " + r.etcList()); // 차상위 관련 문구 확인 가능
                System.out.println("--------------------------------------------------");
            }
        }
    }
}
