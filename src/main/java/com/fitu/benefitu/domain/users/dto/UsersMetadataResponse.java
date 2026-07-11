package com.fitu.benefitu.domain.users.dto;

import com.fitu.benefitu.domain.benefits.types.ResidenceType;

import java.util.List;

public record UsersMetadataResponse(
        List<UsersSchoolDto> schools,
        List<ResidenceType> residences
) {
}
