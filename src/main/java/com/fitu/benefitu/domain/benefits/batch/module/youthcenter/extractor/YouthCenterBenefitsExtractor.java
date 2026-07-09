package com.fitu.benefitu.domain.benefits.batch.module.youthcenter.extractor;

import com.fitu.benefitu.domain.benefits.batch.module.BenefitsExtractor;
import com.fitu.benefitu.domain.benefits.batch.module.RawBenefit;
import com.fitu.benefitu.domain.benefits.types.BenefitStatus;
import com.fitu.benefitu.domain.benefits.types.ExtractorId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class YouthCenterBenefitsExtractor implements BenefitsExtractor {
    // 금액 추출 패턴: "12만 원", "100만원" 등
    private static final Pattern AMOUNT_PATTERN = Pattern.compile("(\\d+)(만)?\\s*원");
    // 날짜 추출 패턴: "YYYYMMDD ~ YYYYMMDD" 에서 뒤쪽 날짜=
    private static final Pattern DATE_PATTERN = Pattern.compile("(\\d{8})");

    @Override
    public ExtractorId extractSourceId(RawBenefit rawBenefit) {
        return ExtractorId.YOUTH_CENTER;
    }

    @Override
    public String extractBenefitName(RawBenefit rawBenefit) {
        return rawBenefit.BenefitName();
    }

    @Override   // 이거 문맥 분석 해야 함
    public Long extractAmount(RawBenefit rawBenefit) {
        // 탐색할 우선순위 리스트
        String[] targets = {rawBenefit.etcList(), rawBenefit.contents()};

        for (String target : targets) {
            if (target == null || target.isBlank()) continue;

            Matcher matcher = AMOUNT_PATTERN.matcher(target);
            if (matcher.find()) {
                try {
                    long amount = Long.parseLong(matcher.group(1));
                    // 2번째 그룹이 '만'이면 10,000을 곱함
                    if (matcher.group(2) != null) {
                        amount *= 10000;
                    }
                    return amount;
                } catch (NumberFormatException e) {
                    continue; // 파싱 실패 시 다음 타겟 탐색
                }
            }
        }
        return 0L; // 금액을 찾지 못했을 경우
    }

    @Override
    public String extractBenefitUrl(RawBenefit rawBenefit) {
        return rawBenefit.BenefitUrl();
    }

    @Override
    public BenefitStatus extractBenefitStatus(RawBenefit rawBenefit, DeadlineResult deadlineResult) {
        if (deadlineResult == null || deadlineResult.status() == null) {
            return BenefitStatus.ERROR;
        }
        if (deadlineResult.isClosed()) {
            return BenefitStatus.CLOSED;
        }
        if (deadlineResult.isAlways()) {
            return BenefitStatus.SAFE_ALWAYS;
        }
        // 남은 경우는 "Date" 형태이므로 기간제 혜택
        return BenefitStatus.SAFE_SCOPED;
    }

    @Override
    public DeadlineResult extractDeadLine(RawBenefit rawBenefit) {
        String deadLineCode = rawBenefit.deadLineCode();

        // 1. 코드 기반 상태 판별(마감/상시)
        if ("0057073".equals(deadLineCode)) {
            return new DeadlineResult(DeadlineResult.STATUS_CLOSED, null);
        }
        if ("0057072".equals(deadLineCode)) {
            return new DeadlineResult(DeadlineResult.STATUS_ALWAYS, null);
        }

        // 2. 텍스트 데이터 기반 날짜 추출
        String period = rawBenefit.deadLine();
        if (period == null || period.isBlank()) {
            return null; // 데이터 없음
        }

        Matcher matcher = DATE_PATTERN.matcher(period);
        String lastDateStr = null;
        while (matcher.find()) {
            lastDateStr = matcher.group(1);
        }

        if (lastDateStr != null) {
            try {
                LocalDate date = LocalDate.of(
                        Integer.parseInt(lastDateStr.substring(0, 4)),
                        Integer.parseInt(lastDateStr.substring(4, 6)),
                        Integer.parseInt(lastDateStr.substring(6, 8))
                );
                if (date.isAfter(LocalDate.now())) {
                    return new DeadlineResult(DeadlineResult.STATUS_DATE, date); // 객체로 감싸서 반환
                }
            } catch (Exception e) {
                return null; // 파싱 실패 시
            }
        }
        return null; // 마감일 정보 없음
    }

    @Override
    public LocalDateTime extractFetchedDate(RawBenefit rawBenefit) {
        return LocalDateTime.now();
    }
}
