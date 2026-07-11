package com.fitu.benefitu.domain.benefits.entity;

import com.fitu.benefitu.domain.benefits.types.SchoolType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class DepartmentConverter implements AttributeConverter<SchoolType.Department, String> {

    @Override
    public String convertToDatabaseColumn(SchoolType.Department department) {
        return (department == null) ? null : department.getDepartmentCode();
    }

    @Override
    public SchoolType.Department convertToEntityAttribute(String code) {
        // 모든 SchoolType을 뒤져서 해당 코드를 가진 Department 객체를 반환
        for (SchoolType type : SchoolType.values()) {
            SchoolType.Department dept = type.getDepartmentByCode(code);
            if (dept != null) return dept;
        }
        return null;
    }
}
