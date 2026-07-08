package com.fitu.benefitu.domain.users.dto;

public record DetailInfoResponse(
        Float gpa,
        Integer incomeBracket,
        Boolean isBasicLiving,
        Boolean inSecondLowest,
        Long Interests
) {
}
