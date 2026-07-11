package com.fitu.benefitu.domain.benefits.types;

import com.fitu.benefitu.domain.users.type.CategoryType;
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

        public String mappingCategoryTreeAndReturnDescription(CategoryType categoryType) {
                if(this.equals(NATIONAL)){
                        if(categoryType.equals(CategoryType.STATE)){
                                return this.description;
                        }
                }
                if(this.equals(CORPORATE)){
                        if(categoryType.equals(CategoryType.CORPORATE)){
                                return this.description;
                        }
                }
                if(this.equals(REGIONAL)){
                        if(categoryType.equals(CategoryType.REGION)){
                                return this.description;
                        }
                }
                if(this.equals(CONDITIONAL)){
                        if(categoryType.equals(CategoryType.REQUIREMENTS)){
                                return this.description;
                        }
                }
                return null;
        }
}
