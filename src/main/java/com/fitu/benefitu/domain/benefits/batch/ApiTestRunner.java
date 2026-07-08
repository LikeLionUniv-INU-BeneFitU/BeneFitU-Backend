package com.fitu.benefitu.domain.benefits.batch;

import com.fitu.benefitu.domain.benefits.batch.engine.ExtractEngine;
import com.fitu.benefitu.domain.benefits.batch.engine.FetchEngine;
import com.fitu.benefitu.domain.benefits.batch.module.RawBenefit;
import com.fitu.benefitu.domain.benefits.batch.service.BatchService;
import com.fitu.benefitu.domain.benefits.entity.Benefits;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ApiTestRunner implements CommandLineRunner {

    private final FetchEngine fetchEngine;
    private final ExtractEngine extractEngine;
    private final BatchService batchService;

    public ApiTestRunner(FetchEngine fetchEngine, ExtractEngine extractEngine, BatchService batchService) {
        this.fetchEngine = fetchEngine;
        this.extractEngine = extractEngine;
        this.batchService = batchService;
    }

    @Override
    public void run(String... args) throws Exception {
//        var response1 = fetchEngine.fetchAllRegions();

//        if(response1!=null && response1.size()>0) {
//            for(var r : response1) {
//                System.out.println("--------------------------------------------------");
//                System.out.println("[정책명]          : " + r.BenefitName());
//                System.out.println("[정책 설명]         : " + r.contents());
//                System.out.println("[신청 URL]        : " + r.BenefitUrl());
//                System.out.println("[신청 기간 코드]         : " + r.deadLineCode());
//                System.out.println("[신청 기간]         : " + r.deadLine());
//                System.out.println("[정책 제공방법]: " + r.applyWay());
//                System.out.println("[학교]: " + r.schoolType());
//                System.out.println("[전공 요건 코드]   : " + r.departmentType());
//                System.out.println("[학력 요건 코드]   : " + r.grade());
//                System.out.println("[최소 지원 연령]   : " + r.minAge());
//                System.out.println("[최대 지원 연령]   : " + r.maxAge());
//                System.out.println("[거주 지역]   : " + r.residence());
//                System.out.println("[학점]   : " + r.gpa());
//                System.out.println("[소득 최소 금액]   : " + r.minAmt());
//                System.out.println("[소득 최대 금액]   : " + r.maxAmt());
//                System.out.println("[기초 생활 수급자 여부]: " + r.isBasicLiving());      // 0014003: 기초생활수급자
//                System.out.println("[기타 사항]   : " + r.etcList()); // 차상위 관련 문구 확인 가능
//                System.out.println("--------------------------------------------------");
//            }

        // test 1
//            System.out.println("데이터 가져오기 성공! 개수: " + response1.size());
//
//            int extractedCount = 0;
//            int scolarshipCount = 0;
//            for(RawBenefit rawBenefit : response1) {
//                if(rawBenefit == null) continue;
//
//                System.out.println("--------------------------------------------------");
//                System.out.println(rawBenefit.BenefitName());
//
//                boolean isScholarship = extractEngine.isScholarship(rawBenefit);
//                System.out.println("[장학금인가?]" + isScholarship);
//                if(isScholarship) {
//                    scolarshipCount++;
//                }
//
//                // 1. 여기서 반환된 benefits가 null일 수 있음을 인지해야 함
//                Benefits benefits = extractEngine.extractBenefits(rawBenefit);
//
//                // 2. null 체크 필수!
//                if (benefits != null) {
//                    extractedCount++;
//                    System.out.println("[추출된 혜택] : " + benefits.getBenefitName());
//                } else {
//                    System.out.println("[알림] : 마감일 정보가 없어 이 혜택은 제외됩니다.");
//                }
//                System.out.println("--------------------------------------------------");
//            }
//            System.out.println("처리한 총 혜택 수 : "+extractedCount);
//            System.out.println("장학 혜택 수 : "+scolarshipCount);

        // test 2 : 장학만 추출
//            System.out.println("데이터 가져오기 성공! 개수: " + response1.size());
//
//            int extractedCount = 0;
//            int scolarshipCount = 0;
//            for(RawBenefit rawBenefit : response1) {
//                if(rawBenefit == null) continue;
//
//                boolean isScholarship = extractEngine.isScholarship(rawBenefit);
//
//                // 1. 여기서 반환된 benefits가 null일 수 있음을 인지해야 함
//                Benefits benefits = extractEngine.extractBenefits(rawBenefit);
//
//                if(isScholarship && benefits!=null) {
//                    scolarshipCount++;
//                }
//                // 2. null 체크 필수!
//                if (benefits != null && isScholarship) {
//                    System.out.println("--------------------------------------------------");
//                    System.out.println(rawBenefit.BenefitName());
//                    extractedCount++;
//                    System.out.println("[마감일] : " + benefits.getDeadLine());
//                    System.out.println("--------------------------------------------------");
//                }
//
//            }
//            System.out.println("처리한 장학 혜택 수 : "+extractedCount);
//            System.out.println("총 장학 혜택 수 : "+scolarshipCount);
//
//            // test 3
//            System.out.println("총 처리 개수 : "+batchService.BatchAll());
//
//      }

    }
}
