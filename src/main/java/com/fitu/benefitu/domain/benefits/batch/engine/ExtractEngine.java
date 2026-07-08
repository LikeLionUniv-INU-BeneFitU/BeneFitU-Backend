package com.fitu.benefitu.domain.benefits.batch.engine;

import com.fitu.benefitu.domain.benefits.batch.module.BenefitsExtractor;
import com.fitu.benefitu.domain.benefits.batch.module.RawBenefit;
import com.fitu.benefitu.domain.benefits.batch.module.youthcenter.extractor.YouthCenterBenefitTargetConditionsExtractor;
import com.fitu.benefitu.domain.benefits.batch.module.youthcenter.extractor.YouthCenterBenefitsExtractor;
import com.fitu.benefitu.domain.benefits.entity.BenefitTargetConditions;
import com.fitu.benefitu.domain.benefits.entity.Benefits;
import com.fitu.benefitu.domain.benefits.types.BenefitStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExtractEngine {
    private final YouthCenterBenefitsExtractor youthCenterBenefitsExtractor;
    private final YouthCenterBenefitTargetConditionsExtractor youthCenterBenefitScoringWeightsExtractor;

    public Benefits extractBenefits(RawBenefit rawBenefit) {
        BenefitsExtractor.DeadlineResult deadline = youthCenterBenefitsExtractor.extractDeadLine(rawBenefit);
        BenefitStatus status = youthCenterBenefitsExtractor.extractBenefitStatus(rawBenefit, deadline);
        return Benefits.builder()
                .sourceId(youthCenterBenefitsExtractor.extractSourceId(rawBenefit))
                .benefitName(youthCenterBenefitsExtractor.extractBenefitName(rawBenefit))
                .amount(youthCenterBenefitsExtractor.extractAmount(rawBenefit))
                .benefitUrl(youthCenterBenefitsExtractor.extractBenefitUrl(rawBenefit))
                .status(status)
                .deadLine(deadline.date())
                .fetchedAt(youthCenterBenefitsExtractor.extractFetchedDate(rawBenefit))
                .build();

    }

    public BenefitTargetConditions extractBenefitTargetConditions(RawBenefit rawBenefit, Benefits benefitId) {
        return BenefitTargetConditions.builder()
                .benefitId(benefitId)
                .schoolType(youthCenterBenefitScoringWeightsExtractor.extractorSchoolType(rawBenefit))
                .departmentType(youthCenterBenefitScoringWeightsExtractor.extractorDepartment(rawBenefit))
                .grade(youthCenterBenefitScoringWeightsExtractor.extractorGrade(rawBenefit))
                .minAge(youthCenterBenefitScoringWeightsExtractor.extractorMinAge(rawBenefit))
                .maxAge(youthCenterBenefitScoringWeightsExtractor.extractorMaxAge(rawBenefit))
                .residenceType(youthCenterBenefitScoringWeightsExtractor.extractorResidenceType(rawBenefit))
                .build();
    }
}
