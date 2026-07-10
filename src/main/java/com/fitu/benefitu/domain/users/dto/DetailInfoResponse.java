package com.fitu.benefitu.domain.users.dto;

import java.util.List;

public record DetailInfoResponse(
        Float gpa,              // 학점
        Integer incomeBracket,  // 소득 분위
        Boolean isBasicLiving,  // 기초생활수급자 여부
        Boolean isSecondLowest, // 차상위 계층 여부
        List<String> interests     // 관심 분야 목록
) {
}
