package com.fitu.benefitu.domain.benefits.repository;

import com.fitu.benefitu.domain.benefits.entity.BenefitTargetConditions;
import com.fitu.benefitu.domain.benefits.entity.Benefits;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BenefitTargetConditionsRepository extends JpaRepository<BenefitTargetConditions, Integer> {
    BenefitTargetConditions findByBenefit(Benefits benefit);
}
