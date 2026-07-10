package com.fitu.benefitu.domain.users.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

public record DetailInfoDto(
        Float gpa,              // 학점
        Integer incomeBracket,  // 소득 분위
        Boolean isBasicLiving,  // 기초생활수급자 여부
        Boolean isSecondLowest, // 차상위 계층 여부
        Interests interests     // 관심 분야 목록
) {
    @Getter
    @AllArgsConstructor
    public static class Interests {
        private final Boolean corporate;    // 기업, 재단 장학금
        private final Boolean region;       // 지역 장학금
        private final Boolean requirements; // 조건별 장학금
        private final Boolean state;        // 국가 장학금
    }
}
