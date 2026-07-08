package com.fitu.benefitu.domain.users.dto;

import java.time.LocalDate;

public record BaseInfoDto(
         String schoolName,
         String department,
         Integer grade,
         String residence,
         LocalDate birthDate
) {
}
