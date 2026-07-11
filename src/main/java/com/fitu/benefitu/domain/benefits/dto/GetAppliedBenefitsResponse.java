package com.fitu.benefitu.domain.benefits.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

public record GetAppliedBenefitsResponse(
        List<AppliedBenefits> appliedBenefits
) {
    @Getter
    @AllArgsConstructor
    public static class AppliedBenefits {
        private final Long benefitId;
        private final String benefitName;
        private final String LocalDate;
        private final String applyStatus;
    }
}
