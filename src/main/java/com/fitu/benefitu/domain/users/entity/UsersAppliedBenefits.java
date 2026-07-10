package com.fitu.benefitu.domain.users.entity;

import com.fitu.benefitu.domain.benefits.entity.Benefits;
import com.fitu.benefitu.domain.users.type.ApplyStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
public class UsersAppliedBenefits {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "USER_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Users user;

    @JoinColumn(name = "BENEFIT_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Benefits benefit;

    private LocalDate appliedAt;

    @Enumerated(EnumType.STRING)
    private ApplyStatus status;
}
