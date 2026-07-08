package com.fitu.benefitu.domain.benefits.batch.module.youthcenter.extractor;

import com.fitu.benefitu.domain.benefits.batch.module.BenefitsTargetConditionsExtractor;
import com.fitu.benefitu.domain.benefits.batch.module.RawBenefit;
import com.fitu.benefitu.domain.benefits.types.ResidenceType;
import com.fitu.benefitu.domain.benefits.types.SchoolType;
import org.springframework.stereotype.Component;

@Component
public class YouthCenterBenefitTargetConditionsExtractor implements BenefitsTargetConditionsExtractor {
    @Override
    public SchoolType extractorSchoolType(RawBenefit rawBenefit) {
        return rawBenefit.schoolType();
    }

    @Override
    public SchoolType.Department extractorDepartment(RawBenefit rawBenefit) {
        return rawBenefit.departmentType();
    }

    @Override
    public Integer extractorGrade(RawBenefit rawBenefit) {
        return 0;   // 0 학정 이상 -> 상관 없음
    }

    @Override
    public Integer extractorMinAge(RawBenefit rawBenefit) {
        return rawBenefit.minAge();
    }

    @Override
    public Integer extractorMaxAge(RawBenefit rawBenefit) {
        return rawBenefit.maxAge();
    }

    @Override
    public ResidenceType extractorResidenceType(RawBenefit rawBenefit) {
        return rawBenefit.residence();
    }
}
