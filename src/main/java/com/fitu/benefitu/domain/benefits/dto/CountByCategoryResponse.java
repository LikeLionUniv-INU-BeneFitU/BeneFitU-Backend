package com.fitu.benefitu.domain.benefits.dto;

public record CountByCategoryResponse(
        Integer corporateCount,
        Integer regionCount,
        Integer requirementsCount,
        Integer stateCount
) {
}
