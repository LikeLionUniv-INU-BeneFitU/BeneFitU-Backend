package com.fitu.benefitu.domain.benefits.batch.module;

import com.fitu.benefitu.domain.benefits.types.ResidenceType;
import com.fitu.benefitu.domain.benefits.types.SchoolType;

public interface BenefitsScoringWeightsExtractor {
    // 학교
    public SchoolType extractorSchoolType(RawBenefit rawBenefit);
    // 학과
    public SchoolType.Department extractorDepartment(RawBenefit rawBenefit);
    // 점수
    public Integer extractorGrade(RawBenefit rawBenefit);
    // 최소 연령
    public Integer extractorMinAge(RawBenefit rawBenefit);
    // 최대 연령
    public Integer extractorMaxAge(RawBenefit rawBenefit);
    // 거주 지역
    public ResidenceType extractorResidenceType(RawBenefit rawBenefit);
}
