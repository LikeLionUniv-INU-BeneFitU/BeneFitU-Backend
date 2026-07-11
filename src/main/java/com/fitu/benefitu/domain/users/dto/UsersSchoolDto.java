package com.fitu.benefitu.domain.users.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

public record UsersSchoolDto(
        Long schoolId,
        String schoolName,
        List<Department> departments
) {
    @Getter
    @AllArgsConstructor
    public static class Department {
        private final String departmentId;
        private final String departmentName;
    }
}
