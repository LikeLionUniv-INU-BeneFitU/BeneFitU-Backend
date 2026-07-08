package com.fitu.benefitu.domain.benefits.batch.module;

import com.fitu.benefitu.domain.benefits.types.ResidenceType;
import com.fitu.benefitu.domain.benefits.types.SchoolType;

public interface BenefitsTargetConditionsExtractor {
    // 학교
    public SchoolType extractSchoolType(RawBenefit rawBenefit);
    // 학과
    public SchoolType.Department extractDepartment(RawBenefit rawBenefit);
    // 학년
    public Integer extractGrade(RawBenefit rawBenefit);
    // 최소 연령
    public Integer extractMinAge(RawBenefit rawBenefit);
    // 최대 연령
    public Integer extractMaxAge(RawBenefit rawBenefit);
    // 거주 지역
    public ResidenceType extractResidenceType(RawBenefit rawBenefit);
}
