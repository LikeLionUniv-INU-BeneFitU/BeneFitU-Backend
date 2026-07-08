package com.fitu.benefitu.domain.benefits.types;

public enum BenefitStatus {
    SAFE_SCOPED,   // 안정된 상태(특정 기간동안)
    SAFE_ALWAYS,   // 안정된 상태(기간 정해지지 않음)
    ERROR,  // 에러 발생
    CLOSED  // 마감된 상태
}
