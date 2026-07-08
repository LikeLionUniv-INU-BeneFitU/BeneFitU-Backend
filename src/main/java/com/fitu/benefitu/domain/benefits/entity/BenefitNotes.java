package com.fitu.benefitu.domain.benefits.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE) // Builder 사용하기 위해 추가
public class BenefitNotes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // 혜택 ID
    @JoinColumn(name = "benefit_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Benefits benefit;

    // 기타 사항 내용
    private String note;
}
