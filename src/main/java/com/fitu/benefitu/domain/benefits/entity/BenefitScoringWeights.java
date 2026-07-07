package com.fitu.benefitu.domain.benefits.entity;

import com.fitu.benefitu.domain.benefits.entity.types.ResidenceType;
import com.fitu.benefitu.domain.benefits.entity.types.SchoolType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

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
    @ManyToOne
    private Benefits benefitId;

    // 학교 명
    @Enumerated(EnumType.STRING)
    private SchoolType schoolType;

    // 학과 ID
    private Integer departmentId;

    // 점수
    private Integer grade;

    // 최소 연령 - 생년월일
    private LocalDate birthDate;

    // 거주 지역(도 단위)
    @Enumerated(EnumType.STRING)
    private ResidenceType residenceType;
}
