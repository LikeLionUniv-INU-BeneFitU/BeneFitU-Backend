package com.fitu.benefitu.domain.benefits.batch.service;

import com.fitu.benefitu.domain.benefits.batch.engine.ExtractEngine;
import com.fitu.benefitu.domain.benefits.batch.engine.FetchEngine;
import com.fitu.benefitu.domain.benefits.batch.module.RawBenefit;
import com.fitu.benefitu.domain.benefits.entity.*;
import com.fitu.benefitu.domain.benefits.repository.*;
import com.fitu.benefitu.domain.benefits.types.BenefitCategory;
import com.fitu.benefitu.domain.benefits.types.BenefitStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BatchService {
    private final FetchEngine fetchEngine;
    private final ExtractEngine extractEngine;
    private final BenefitsRepository benefitsRepository;


    public int BatchAll() {
        int count = 0;
        List<RawBenefit> rawBenefits = fetchEngine.fetchAllRegions();

        // test 2 : 장학만 추출
        for (RawBenefit rawBenefit : rawBenefits) {
            if (rawBenefit == null) continue;

            if(!extractEngine.isScholarship(rawBenefit)){
                continue;
            }

            // benefits 추출
            // 1. 여기서 반환된 benefits가 null일 수 있음을 인지해야 함
            Benefits benefits = extractEngine.extractBenefits(rawBenefit);
            // 2. null 체크
            if (benefits == null) {
                continue;
            }
            System.out.println("--------------------------------------------------");
            System.out.println("[추출된 혜택] : " + benefits.getBenefitName());

            // benefitTargetConditions 추출
            BenefitTargetConditions benefitTargetConditions = extractEngine.extractBenefitTargetConditions(rawBenefit, benefits);
            if (benefitTargetConditions == null) {
                continue;
            }
            System.out.println("[benefitTargetConditions 추출] : 성공");
            benefits.addTargetConditions(benefitTargetConditions);

            // benefitScoringWeights 추출
            BenefitScoringWeights benefitScoringWeights = extractEngine.extractBenefitScoringWeights(rawBenefit, benefits);
            if (benefitScoringWeights == null) {
                continue;
            }
            System.out.println("[benefitScoringWeights 추출] : 성공");
            benefits.addScoringWeights(benefitScoringWeights);

            // benefitCategories 추출
            List<BenefitCategory> categories = extractEngine.extractCategories(rawBenefit);
            List<BenefitCategories> benefitCategories = new ArrayList<>();
            for (BenefitCategory benefitCategory : categories) {
                benefitCategories.add(BenefitCategories.builder()
                        .benefit(benefits)
                        .benefitCategory(benefitCategory)
                        .build());
            }
            System.out.println("[benefitCategories 추출] : 성공 / " + benefitCategories.size());
            benefitCategories.forEach(benefits::addCategory);

            // benefitNotes 추출
            List<BenefitNotes> targetConditions = extractEngine.extractBenefitNotes(rawBenefit, benefits);
            System.out.println("[benefitNotes 추출] : 성공 / " + targetConditions.size());
            targetConditions.forEach(benefits::addNote);

            count++;
            benefitsRepository.save(benefits);
        }
        return count;
    }
}
