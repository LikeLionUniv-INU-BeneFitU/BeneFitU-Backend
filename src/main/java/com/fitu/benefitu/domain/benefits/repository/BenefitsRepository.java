package com.fitu.benefitu.domain.benefits.repository;

import com.fitu.benefitu.domain.benefits.entity.Benefits;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BenefitsRepository extends JpaRepository<Benefits,Long> {
}
