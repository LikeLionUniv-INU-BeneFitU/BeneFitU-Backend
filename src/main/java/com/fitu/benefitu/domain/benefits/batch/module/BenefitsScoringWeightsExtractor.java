package com.fitu.benefitu.domain.benefits.batch.module;

public interface BenefitsScoringWeightsExtractor {
    // 학점
    public Float extractGpa(RawBenefit rawBenefit);
    // 소득 순위
    public Integer extractIncomeBracket(RawBenefit rawBenefit);
    // 기초 생활 수급자 여부
    public Boolean isBasicLiving(RawBenefit rawBenefit);
    // 차상위 계층 여부
    public Boolean isSecondLowest(RawBenefit rawBenefit);
}
