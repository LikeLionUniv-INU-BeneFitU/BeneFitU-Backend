package com.fitu.benefitu.domain.benefits.types;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
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
            // 상경계열 (0011003) - 6
            new Department("0011003", "경영학부"),
            new Department("0011003", "데이터과학과"), // 상경/사회 교차영역이나 상경분류
            new Department("0011003", "세무회계학과"),
            new Department("0011003", "Global Trade & Service학부"),
            new Department("0011003", "경제학과"),
            new Department("0011003", "동북아국제통상전공"),

            // 사회계열 (0011002) - 12
            new Department("0011002", "소비자학과"),
            new Department("0011002", "정치외교학과"),
            new Department("0011002", "행정학과"),
            new Department("0011002", "스마트물류공학전공"),
            new Department("0011002", "도시행정학과"),
            new Department("0011002", "법학부"),
            new Department("0011002", "문헌정보학과"),
            new Department("0011002", "미디어커뮤니케이션학과"),
            new Department("0011002", "사회복지학과"),
            new Department("0011002", "창의인재개발학과"),
            new Department("0011002", "일본지역문화학과"),
            new Department("0011002", "중어중국학과"),

            // 인문계열 (0011001) - 10
            new Department("0011001", "국어교육과"),
            new Department("0011001", "역사교육과"),
            new Department("0011001", "영어교육과"),
            new Department("0011001", "유아교육과"),
            new Department("0011001", "윤리교육과"),
            new Department("0011001", "일어교육과"),
            new Department("0011001", "국어국문학과"),
            new Department("0011001", "독어독문학과"),
            new Department("0011001", "불어불문학과"),
            new Department("0011001", "영어영문학과"),

            // 공학계열 (0011005) - 17
            new Department("0011005", "기계공학과"),
            new Department("0011005", "바이오-로봇시스템공학과"),
            new Department("0011005", "산업경영공학과"),
            new Department("0011005", "신소재공학과"),
            new Department("0011005", "안전공학과"),
            new Department("0011005", "에너지화학공학과"),
            new Department("0011005", "전기공학과"),
            new Department("0011005", "전자공학부"),
            new Department("0011005", "도시건축학부"),
            new Department("0011005", "도시공학과"),
            new Department("0011005", "도시환경공학부(건설환경공학전공)"),
            new Department("0011005", "도시환경공학부(환경공학전공)"),
            new Department("0011005", "컴퓨터공학부"),
            new Department("0011005", "인공지능시스템공학과"),
            new Department("0011005", "인공지능정보통신학부"),
            new Department("0011005", "생명공학부(생명공학전공)"),
            new Department("0011005", "생명공학부(나노바이오공학전공)"),

            // 이학계열 (0011004) - 9
            new Department("0011004", "수학교육과"),
            new Department("0011004", "수학과"),
            new Department("0011004", "생명과학부(생명과학전공)"),
            new Department("0011004", "생명과학부(분자의생명전공)"),
            new Department("0011004", "물리학과"),
            new Department("0011004", "화학과"),
            new Department("0011004", "해양학과"),
            new Department("0011004", "운동건강학부"),
            new Department("0011004", "패션산업학과"), // 누락되었던 학과 포함

            // 예체능계열 (0011006) - 7
            new Department("0011006", "공연예술학과"),
            new Department("0011006", "디자인학부"),
            new Department("0011006", "스포츠과학부"),
            new Department("0011006", "스포츠의학부"),
            new Department("0011006", "조형예술학부(서양화전공)"),
            new Department("0011006", "조형예술학부(한국화전공)"),
            new Department("0011006", "체육교육과"),

            // 기타(0011008) - 2
            new Department("0011008", "자유전공학부(인문)"),
            new Department("0011008", "자유전공학부(자연)")
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

    public static boolean checkSchoolName(String schoolName) {
        if (schoolName == null || schoolName.isEmpty()) return false;
        for(SchoolType type : values()) {
            if (type.getSchoolName().equals(schoolName)) return true;
        }
        return false;
    }

    public static boolean checkDepartments(String departmentName) {
        if (departmentName == null) return false; // 방어 코드 추가

        for(Department department : SchoolType.INCHEON.getDepartments()) {
            // 순서를 바꾸면 departmentName이 null이어도 안전하게 false를 반환합니다.
            if(department.getDepartmentName().equals(departmentName)) {
                return true;
            }
        }
        return false;
    }
}
