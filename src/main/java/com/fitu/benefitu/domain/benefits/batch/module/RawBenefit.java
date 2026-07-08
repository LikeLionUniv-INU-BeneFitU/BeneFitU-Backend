package com.fitu.benefitu.domain.benefits.batch.module;

import com.fitu.benefitu.domain.benefits.batch.module.youthcenter.dto.YouthPolicyApiResponse;
import com.fitu.benefitu.domain.benefits.types.ResidenceType;
import com.fitu.benefitu.domain.benefits.types.SchoolType;

public record RawBenefit(
        // 혜택 내용
        // 정책명
        String BenefitName,
        // 내용(정책 내용 + 기타 사항)
        String contents,
        // 신청 url
        String BenefitUrl,
        // 신청 기간 유무
        String deadLineCode,
        // 신청 기간
        String deadLine,
        // 제공 방법 -> 카테고리 활용 & 금액 유무 판단
        String applyWay,

        // 제약 조건
        // 학교
        SchoolType schoolType,
        // 전공
        SchoolType.Department departmentType,
        // 학년
        String grade,
        // 나이
        Integer minAge,
        Integer maxAge,
        // 거주 지역
        ResidenceType residence,

        // 점수 가중치 목록
        // 학점
        Float gpa,
        // 소득 분위
        Long minAmt,
        Long maxAmt,
        // 기초 생활 수급자 -> false라도 기초 생활 수급자는 접근 가능 -> 2차 검증 필요
        Boolean isBasicLiving,
        // 기타 사항(소득기타내용, 추가 신청, 참여 제한 대상)
        // -> 소득 분위, 기초 생활, 차상위 분별에 활용될 비정형 데이터 집합소
        String etcList
) {
    public static RawBenefit from(YouthPolicyApiResponse.YouthPolicy dto, ResidenceType residence) {
        // 1. null 안전한 변환을 위한 유틸 메서드
        String sbizCd = (dto.getSbizCd() != null) ? dto.getSbizCd() : "";
        String aplyPrdSeCd = (dto.getAplyPrdSeCd() != null) ? dto.getAplyPrdSeCd() : "";

        // 2. 신청 기간 로직
        String deadLine = (dto.getAplyYmd() != null && !dto.getAplyYmd().isEmpty())
                ? dto.getAplyYmd()
                : dto.getBizPrdEndYmd();

        // 3. 기초생활수급자 체크 -> 완전하지 않기에, false 반환시 기타 사항 검사해야 한다.
        boolean isBasicLiving = "0014003".equals(sbizCd);

        // 4. 나이 파싱 (NumberFormatException 방지)
        Integer minAge = parseIntegerSafe(dto.getSprtTrgtMinAge());
        Integer maxAge = parseIntegerSafe(dto.getSprtTrgtMaxAge());

        // 5. 기타 사항 통합 (null 처리)
        String etcContent = String.format("%s\n%s\n%s",
                dto.getEarnEtcCn() != null ? dto.getEarnEtcCn() : "",
                dto.getAddAplyQlfcCndCn() != null ? dto.getAddAplyQlfcCndCn() : "",
                dto.getPtcpPrpTrgtCn() != null ? dto.getPtcpPrpTrgtCn() : "");

        return new RawBenefit(
                dto.getPlcyNm(),
                (dto.getPlcyExplnCn() != null ? dto.getPlcyExplnCn() : "") + "\n" +
                        (dto.getEtcMttrCn() != null ? dto.getEtcMttrCn() : ""),
                dto.getAplyUrlAddr(),
                aplyPrdSeCd,
                deadLine,
                dto.getPlcyPvsnMthdCd(),
                SchoolType.STANDARD, // 필요시 별도 파싱 로직 추가
                SchoolType.STANDARD.getDepartmentByCode(dto.getPlcyMajorCd()), // 학과
                dto.getSchoolCd(), // grade 파싱 (학년 로직)
                minAge,
                maxAge,
                residence,
                null,   // 학점
                parseLongSafe(dto.getEarnMinAmt()),
                parseLongSafe(dto.getEarnMaxAmt()),
                isBasicLiving,
                etcContent
        );
    }

    // 안전 파싱을 위한 보조 메서드들
    private static Integer parseIntegerSafe(String value) {
        try {
            return (value == null || value.isBlank()) ? null : Integer.parseInt(value.replaceAll("[^0-9]", ""));
        } catch (Exception e) {
            return null;
        }
    }

    private static Long parseLongSafe(String value) {
        try {
            return (value == null || value.isBlank()) ? null : Long.parseLong(value.replaceAll("[^0-9]", ""));
        } catch (Exception e) {
            return null;
        }
    }
}
