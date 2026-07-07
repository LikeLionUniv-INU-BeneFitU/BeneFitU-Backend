package com.fitu.benefitu.domain.benefits.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE) // Builder 사용하기 위해 추가
public class BenefitTargetConditions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 학점
    private Float gpa;

    // 소득 순위
    private Integer incomeBracket;

    // 기초 생활 수급자 여부
    private Boolean isBasicLiving;

    // 차상위 계층 여부
    private Boolean isSecondLowest;
}
