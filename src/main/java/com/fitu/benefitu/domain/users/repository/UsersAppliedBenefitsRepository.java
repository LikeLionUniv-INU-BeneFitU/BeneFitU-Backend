package com.fitu.benefitu.domain.users.repository;

import com.fitu.benefitu.domain.users.entity.UsersAppliedBenefits;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersAppliedBenefitsRepository extends JpaRepository<UsersAppliedBenefits, Integer> {
}
