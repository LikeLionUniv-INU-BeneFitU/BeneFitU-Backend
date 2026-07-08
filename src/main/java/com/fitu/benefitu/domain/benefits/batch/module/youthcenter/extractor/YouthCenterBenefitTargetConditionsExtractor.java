package com.fitu.benefitu.domain.benefits.batch.module.youthcenter.extractor;

import com.fitu.benefitu.domain.benefits.batch.module.BenefitsTargetConditionsExtractor;
import com.fitu.benefitu.domain.benefits.batch.module.RawBenefit;
import com.fitu.benefitu.domain.benefits.types.ResidenceType;
import com.fitu.benefitu.domain.benefits.types.SchoolType;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class YouthCenterBenefitTargetConditionsExtractor implements BenefitsTargetConditionsExtractor {
    @Override
    public SchoolType extractSchoolType(RawBenefit rawBenefit) {
        return rawBenefit.schoolType();
    }

    @Override
    public SchoolType.Department extractDepartment(RawBenefit rawBenefit) {
        return rawBenefit.departmentType();
    }

    @Override
    public Integer extractGrade(RawBenefit rawBenefit) {
        String gradeCode = rawBenefit.grade();

        // 해당 사항 없으면 -1 처리
        Integer grade = GRADE_MAPPING.getOrDefault(gradeCode, -1);

        return grade;
    }

    private static final Map<String, Integer> GRADE_MAPPING = Map.of(
            "0049004", 1, // 고졸
            "0049005", 1, // 대학 재학 (전 학년 허용으로 가정)
            "0049006", 4, // 대졸 예정
            "0049007", 5, // 대학 졸업
            "0049008", 5, // 석사/박사
            "0049010", 1  // 제한 없음
    );

    @Override
    public Integer extractMinAge(RawBenefit rawBenefit) {
        return rawBenefit.minAge();
    }

    @Override
    public Integer extractMaxAge(RawBenefit rawBenefit) {
        return rawBenefit.maxAge();
    }

    @Override
    public ResidenceType extractResidenceType(RawBenefit rawBenefit) {
        return rawBenefit.residence();
    }
}
