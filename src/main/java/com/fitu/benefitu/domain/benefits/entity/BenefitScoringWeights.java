package com.fitu.benefitu.domain.benefits.entity;


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
    @JoinColumn(name = "benefit_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Benefits benefit;

    // 학점(최소 요구 학점)
    private Float gpa;

    // 소득 순위(등급) -> 이하
    private Integer incomeBracket;

    // 기초 생활 수급자 여부
    private Boolean isBasicLiving;

    // 차상위 계층 여부
    private Boolean isSecondLowest;
}
