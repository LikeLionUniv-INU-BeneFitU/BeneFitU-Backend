package com.fitu.benefitu.domain.benefits.types;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public enum SchoolType {
    STANDARD(0L, "API 기준 전공 계열 리스트", List.of(
            new Department("0011001", "인문계열"),
            new Department("0011002", "사회계열"),
            new Department("0011003", "상경계열"),
            new Department("0011004", "이학계열"),
            new Department("0011005", "공학계열"),
            new Department("0011006", "예체능계열"),
            new Department("0011007", "농산업계열"),
            new Department("0011008", "기타"),
            new Department("0011009", "제한없음")
    )),
    INCHEON(1L, "인천대학교", List.of(
            new Department("501", "컴퓨터공학과"),
            new Department("502", "정보통신학과")
    ));

    private final Long schoolId;
    private final String schoolName;
    private final List<Department> departments;

    // 내부 클래스로 Department 정의
    @Getter
    @AllArgsConstructor
    public static class Department {
        private final String departmentCode;
        private final String departmentName;
    }

    public Department getDepartmentByCode(String code) {
        if (code == null || code.isEmpty()) return null;
        return this.getDepartments().stream()
                .filter(d -> d.getDepartmentCode().equals(code))
                .findFirst()
                .orElse(null);
    }
}
