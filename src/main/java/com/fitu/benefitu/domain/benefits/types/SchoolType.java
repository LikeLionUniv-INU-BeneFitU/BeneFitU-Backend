package com.fitu.benefitu.domain.benefits.types;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public enum SchoolType {
    INCHEON(1, "인천대학교", List.of(
            new Department(101, "컴퓨터공학과"),
            new Department(102, "정보통신학과")
    ));

    private final int schoolId;
    private final String schoolName;
    private final List<Department> departments;

    // 내부 클래스로 Department 정의
    @Getter
    @AllArgsConstructor
    public static class Department {
        private final int departmentId;
        private final String departmentName;
    }
}
