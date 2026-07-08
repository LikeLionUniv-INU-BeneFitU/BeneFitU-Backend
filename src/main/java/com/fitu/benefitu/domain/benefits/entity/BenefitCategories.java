package com.fitu.benefitu.domain.benefits.entity;

import com.fitu.benefitu.domain.benefits.types.BenefitCategory;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE) // Builder 사용하기 위해 추가
public class BenefitCategories {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 혜택 ID
    @JoinColumn(name = "benefit_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Benefits benefit;

    // 혜택 카테고리
    @Enumerated(EnumType.STRING)
    private BenefitCategory benefitCategory;
}
