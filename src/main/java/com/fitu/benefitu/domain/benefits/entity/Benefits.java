package com.fitu.benefitu.domain.benefits.entity;

import com.fitu.benefitu.domain.benefits.types.BenefitStatus;
import com.fitu.benefitu.domain.benefits.types.ExtractorId;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE) // Builder 사용하기 위해 추가
public class Benefits {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 추출기 고유 번호
    @Enumerated(EnumType.STRING)
    private ExtractorId sourceId;

    // 혜택 이름
    private String benefitName;

    // 금액
    private Long amount;

    // 관련 사이트
    private String benefitUrl;

    // 해당 혜택 상태(DB 상태) : SAFE / FETCHING / ERROR
    @Enumerated(EnumType.STRING)
    private BenefitStatus status;

    // 해당 혜택 마감일
    private LocalDate deadLine;

    // 패치한 날
    private LocalDateTime fetchedAt;
}
