package com.fitu.benefitu.domain.benefits.entity;

import com.fitu.benefitu.domain.benefits.types.BenefitStatus;
import com.fitu.benefitu.domain.benefits.types.ExtractorId;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    // 해당 혜택 상태(DB 상태)
    @Enumerated(EnumType.STRING)
    private BenefitStatus status;

    // 해당 혜택 마감일
    private LocalDate deadLine;

    // 패치한 날
    private LocalDateTime fetchedAt;

    // 추가: 부모가 삭제되면 자식들도 함께 삭제되도록(orphanRemoval = true)
    @OneToOne(mappedBy = "benefit", cascade = CascadeType.ALL, orphanRemoval = true)
    private BenefitTargetConditions targetConditions;

    @OneToOne(mappedBy = "benefit", cascade = CascadeType.ALL, orphanRemoval = true)
    private BenefitScoringWeights scoringWeights;

    @OneToMany(mappedBy = "benefit", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default // Builder 사용 시 빈 리스트로 초기화되도록 설정
    private List<BenefitCategories> categories = new ArrayList<>();

    @OneToMany(mappedBy = "benefit", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<BenefitNotes> notes = new ArrayList<>();

    public void addTargetConditions(BenefitTargetConditions targetConditions) {
        this.targetConditions = targetConditions;
    }

    public void addScoringWeights(BenefitScoringWeights scoringWeights) {
        this.scoringWeights = scoringWeights;
    }

    public void addCategory(BenefitCategories category) {
        this.categories.add(category);
    }

    public void addNote(BenefitNotes note) {
        this.notes.add(note);
    }
}
