package com.fitu.benefitu.domain.users.repository;

import com.fitu.benefitu.domain.benefits.entity.Benefits;
import com.fitu.benefitu.domain.users.entity.Users;
import com.fitu.benefitu.domain.users.entity.UsersAppliedBenefits;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsersAppliedBenefitsRepository extends JpaRepository<UsersAppliedBenefits, Long> {
    boolean existsByUserAndBenefit(Users user, Benefits benefit);
    UsersAppliedBenefits findByUserAndBenefit(Users user, Benefits benefit);
    List<UsersAppliedBenefits> findByUser(Users user);
    List<UsersAppliedBenefits> findByBenefit(Benefits benefit);
}
