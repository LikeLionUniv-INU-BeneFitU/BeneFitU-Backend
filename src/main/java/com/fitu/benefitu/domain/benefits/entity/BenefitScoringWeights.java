package com.fitu.benefitu.domain.benefits.entity;

import com.fitu.benefitu.domain.benefits.types.ResidenceType;
import com.fitu.benefitu.domain.benefits.types.SchoolType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE) // Builder 사용하기 위해 추가
public class BenefitScoringWeights {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 혜택 ID
    @JoinColumn
    @OneToOne
    private Benefits benefitId;

    // 학교 명
    @Enumerated(EnumType.STRING)
    private SchoolType schoolType;

    // 학과
    private SchoolType.Department departmentType;

    // 점수
    private Integer grade;

    // 최소 연령
    private Integer minAge;

    // 최대 연령
    private Integer maxAge;

    // 거주 지역(도 단위)
    @Enumerated(EnumType.STRING)
    private ResidenceType residenceType;
}
