package com.fitu.benefitu.domain.benefits.batch.engine;

import com.fitu.benefitu.domain.benefits.batch.module.BenefitsExtractor;
import com.fitu.benefitu.domain.benefits.batch.module.RawBenefit;
import com.fitu.benefitu.domain.benefits.batch.module.youthcenter.extractor.YouthCenterBenefitTargetConditionsExtractor;
import com.fitu.benefitu.domain.benefits.batch.module.youthcenter.extractor.YouthCenterBenefitsCategoryExtractor;
import com.fitu.benefitu.domain.benefits.batch.module.youthcenter.extractor.YouthCenterBenefitsExtractor;
import com.fitu.benefitu.domain.benefits.batch.module.youthcenter.extractor.YouthCenterBenefitsScoringWeightsExtractor;
import com.fitu.benefitu.domain.benefits.entity.BenefitNotes;
import com.fitu.benefitu.domain.benefits.entity.BenefitScoringWeights;
import com.fitu.benefitu.domain.benefits.entity.BenefitTargetConditions;
import com.fitu.benefitu.domain.benefits.entity.Benefits;
import com.fitu.benefitu.domain.benefits.types.BenefitCategory;
import com.fitu.benefitu.domain.benefits.types.BenefitStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class ExtractEngine {
    private final YouthCenterBenefitsExtractor youthCenterBenefitsExtractor;
    private final YouthCenterBenefitTargetConditionsExtractor youthCenterBenefitTargetConditionsExtractor;
    private final YouthCenterBenefitsScoringWeightsExtractor youthCenterBenefitsScoringWeightsExtractor;
    private final YouthCenterBenefitsCategoryExtractor youthCenterBenefitsCategoryExtractor;

    private static final Pattern SCHOLARSHIP_PATTERN = Pattern.compile("장학|학비|등록금|응시료|교육보조금|훈련보조금|구직보조금|활동수당");

    public boolean isScholarship(RawBenefit rawBenefit) {
        String combinedText = (rawBenefit.BenefitName() + " " + rawBenefit.contents());

        // 패턴 매칭으로 '장학금' 키워드가 하나라도 발견되면 true
        return SCHOLARSHIP_PATTERN.matcher(combinedText).find();
    }

    public Benefits extractBenefits(RawBenefit rawBenefit) {
        BenefitsExtractor.DeadlineResult deadline = youthCenterBenefitsExtractor.extractDeadLine(rawBenefit);
        if(deadline == null) {return null;}

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
        Integer grade = youthCenterBenefitTargetConditionsExtractor.extractGrade(rawBenefit);
        // 대학생이 접근할 수 없는 혜택일 경우
        if (grade == null) {
            return null;
        }
        return BenefitTargetConditions.builder()
                .benefit(benefitId)
                .schoolType(youthCenterBenefitTargetConditionsExtractor.extractSchoolType(rawBenefit))
                .departmentType(youthCenterBenefitTargetConditionsExtractor.extractDepartment(rawBenefit))
                .grade(grade)
                .minAge(youthCenterBenefitTargetConditionsExtractor.extractMinAge(rawBenefit))
                .maxAge(youthCenterBenefitTargetConditionsExtractor.extractMaxAge(rawBenefit))
                .residenceType(youthCenterBenefitTargetConditionsExtractor.extractResidenceType(rawBenefit))
                .build();
    }

    public BenefitScoringWeights extractBenefitScoringWeights(RawBenefit rawBenefit, Benefits benefitId) {
        return BenefitScoringWeights.builder()
                .benefit(benefitId)
                .gpa(youthCenterBenefitsScoringWeightsExtractor.extractGpa(rawBenefit))
                .incomeBracket(youthCenterBenefitsScoringWeightsExtractor.extractIncomeBracket(rawBenefit))
                .isBasicLiving(youthCenterBenefitsScoringWeightsExtractor.isBasicLiving(rawBenefit))
                .isSecondLowest(youthCenterBenefitsScoringWeightsExtractor.isSecondLowest(rawBenefit))
                .build();
    }

    public List<BenefitCategory> extractCategories(RawBenefit raw) {
        List<BenefitCategory> categories = new ArrayList<>();

        // 카테고리 분류 로직
        if (Boolean.TRUE.equals(youthCenterBenefitsCategoryExtractor.isNational(raw))) {
            categories.add(BenefitCategory.NATIONAL);
        }
        if (Boolean.TRUE.equals(youthCenterBenefitsCategoryExtractor.isRegional(raw))) {
            categories.add(BenefitCategory.REGIONAL);
        }
        if(Boolean.TRUE.equals(youthCenterBenefitsCategoryExtractor.isCorporate(raw))) {
            categories.add(BenefitCategory.CORPORATE);
        }
        if(Boolean.TRUE.equals(youthCenterBenefitsCategoryExtractor.isConditional(raw))) {
            categories.add(BenefitCategory.CONDITIONAL);
        }

        return categories;
    }

    public List<BenefitNotes> extractBenefitNotes(RawBenefit raw, Benefits benefit) {
        // 1. 기타 사항 확보
        String rawNotes = raw.etcList();

        // 2. 줄바꿈으로 나눔
        String[] lines = rawNotes.split("\n");

        List<BenefitNotes> noteList = new ArrayList<>();

        for (String line : lines) {
            // 공백이거나 유효하지 않은 줄은 제외
            if (line == null || line.trim().isEmpty() || line.equals("null")) {
                continue;
            }

            // 3. 엔티티 생성
            BenefitNotes note = BenefitNotes.builder()
                    .benefit(benefit)
                    .note(line.trim()) // 불필요한 앞뒤 공백 제거
                    .build();

            noteList.add(note);
        }

        return noteList;
    }
}
