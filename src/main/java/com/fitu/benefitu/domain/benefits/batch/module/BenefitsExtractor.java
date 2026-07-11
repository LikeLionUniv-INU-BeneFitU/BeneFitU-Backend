package com.fitu.benefitu.domain.benefits.batch.module;

import com.fitu.benefitu.domain.benefits.types.BenefitStatus;
import com.fitu.benefitu.domain.benefits.types.ExtractorId;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface BenefitsExtractor {
    // 추출기 번호
    public ExtractorId extractSourceId(RawBenefit rawBenefit);
    // 혜택 이름
    public String extractBenefitName(RawBenefit rawBenefit);
    // 금액
    public Long extractAmount(RawBenefit rawBenefit);
    // 관련 사이트
    public String extractBenefitUrl(RawBenefit rawBenefit);
    // 혜택 상태
    public BenefitStatus extractBenefitStatus(RawBenefit rawBenefit, DeadlineResult deadlineResult);
    // 혜택 마감일
    public DeadlineResult extractDeadLine(RawBenefit rawBenefit);
    // 패치한 날
    public LocalDateTime extractFetchedDate(RawBenefit rawBenefit);

    // 마감일 정보를 담을 작은 DTO
    public record DeadlineResult(String status, LocalDate date) {
        public static final String STATUS_CLOSED = "Closed";
        public static final String STATUS_ALWAYS = "AtAllTimes";
        public static final String STATUS_DATE = "Date";

        public boolean isClosed() { return STATUS_CLOSED.equals(status); }
        public boolean isAlways() { return STATUS_ALWAYS.equals(status); }
    }
}
