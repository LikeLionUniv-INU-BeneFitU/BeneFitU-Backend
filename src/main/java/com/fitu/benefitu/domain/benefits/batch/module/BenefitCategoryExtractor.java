package com.fitu.benefitu.domain.benefits.batch.module;

public interface BenefitCategoryExtractor {
    public Boolean isNational(RawBenefit rawBenefit);
    public Boolean isCorporate(RawBenefit rawBenefit);
    public Boolean isRegional(RawBenefit rawBenefit);
    public Boolean isConditional(RawBenefit rawBenefit);
}