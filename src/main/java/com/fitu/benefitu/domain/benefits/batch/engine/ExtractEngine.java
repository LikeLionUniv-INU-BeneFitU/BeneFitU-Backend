package com.fitu.benefitu.domain.benefits.batch.engine;

import com.fitu.benefitu.domain.benefits.batch.module.BenefitsExtractor;
import com.fitu.benefitu.domain.benefits.batch.module.RawBenefit;
import com.fitu.benefitu.domain.benefits.batch.module.youthcenter.extractor.YouthCenterExtractor;
import com.fitu.benefitu.domain.benefits.entity.Benefits;
import com.fitu.benefitu.domain.benefits.types.BenefitStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExtractEngine {
    private final YouthCenterExtractor youthCenterExtractor;

    public Benefits extractBenefits(RawBenefit rawBenefit) {
        BenefitsExtractor.DeadlineResult deadline = youthCenterExtractor.extractDeadLine(rawBenefit);
        BenefitStatus status = youthCenterExtractor.extractBenefitStatus(rawBenefit, deadline);
        Benefits benefits = Benefits.builder()
                .sourceId(youthCenterExtractor.extractSourceId(rawBenefit))
                .benefitName(youthCenterExtractor.extractBenefitName(rawBenefit))
                .amount(youthCenterExtractor.extractAmount(rawBenefit))
                .benefitUrl(youthCenterExtractor.extractBenefitUrl(rawBenefit))
                .status(status)
                .deadLine(deadline.date())
                .fetchedAt(youthCenterExtractor.extractFetchedDate(rawBenefit))
                .build();

    }
}
