package com.fitu.benefitu.domain.benefits.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

public record GetBenefitsDetailsResponse(
        boolean hasDetails,
        BenefitsDetails benefitDetails,
        MatchedConditions matchedConditions,
        String passProbability

) {
    @Getter
    @AllArgsConstructor
    public static class BenefitsDetails {
        private final Long benefitId;
        private final String benefitName;
        private final String category;
        private final String amount;
        private final String deadLine;
        private final String benefitUrl;
        private final List<String> notes;
    }

    @Getter
    @AllArgsConstructor
    public static class MatchedConditions {
        private final String gpa;
        private final String incomeBracket;
        private final String isBasicLiving;
        private final String isSecondLowest;
    }
}
