package com.fitu.benefitu.domain.benefits.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

public record GetBenefitListResponse(
        int totPages,
        List<Benefits> benefits
) {
    @Getter
    @AllArgsConstructor
    public static class Benefits {
        private final Long benefitId;
        private final String benefitName;
        private final List<String> categories;
        private final String dDay;
        private final String amount;
    }
}
