package com.fitu.benefitu.domain.users.dto;

import com.fitu.benefitu.domain.benefits.types.ResidenceType;

import java.time.LocalDate;

public record BaseInfoDto(
        String schoolName,
        String department,
        Integer grade,
        String residence,
        LocalDate birthDate
) {
}
