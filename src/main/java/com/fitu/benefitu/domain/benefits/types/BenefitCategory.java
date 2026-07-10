package com.fitu.benefitu.domain.benefits.types;

import lombok.Getter;

@Getter
public enum BenefitCategory {
        NATIONAL("국가장학금"),
        CORPORATE("기업재단장학금"),
        REGIONAL("지역장학금"),
        CONDITIONAL("조건별장학금");

        private final String description;

        BenefitCategory(String description) {
            this.description = description;
        }

}
