package com.fitu.benefitu.domain.users.dto;

public record UsersSubmitInfoRequest(
        BaseInfoDto baseInfo,
        DetailInfoDto detailInfo
) {
    public record BaseInfoDto(
            String schoolName,
            String department,
            Integer grade,
            String residence,
            String birthDate
    ) {}

    public record DetailInfoDto(
            Double gpa,
            Integer incomeBracket,
            Boolean isBasicLiving,
            Boolean isSecondLowest,
            InterestsDto Interests
    ) {}
}