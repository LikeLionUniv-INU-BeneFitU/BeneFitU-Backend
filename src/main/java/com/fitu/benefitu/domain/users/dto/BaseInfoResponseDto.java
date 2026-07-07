package com.fitu.benefitu.domain.users.dto;

import java.time.LocalDate;

public record BaseInfoResponseDto(
        String name,
        String schoolName,
        String department,
        Integer grade,
        String residence,
        LocalDate birthDate
) {
}