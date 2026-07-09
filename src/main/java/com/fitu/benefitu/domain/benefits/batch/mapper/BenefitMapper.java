package com.fitu.benefitu.domain.benefits.batch.mapper;

import com.fitu.benefitu.domain.benefits.batch.module.youthcenter.dto.YouthPolicyApiResponse;
import com.fitu.benefitu.domain.benefits.types.BenefitStatus;
import com.fitu.benefitu.domain.benefits.entity.Benefits;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class BenefitMapper {
    public Benefits toEntity(YouthPolicyApiResponse.YouthPolicy apiData) {
        return Benefits.builder()
                .status(BenefitStatus.SAFE_SCOPED)
                // deadLine은 String -> LocalDateTime 파싱 로직 필요
                .deadLine(parseDate(apiData.getBizPrdEndYmd()))
                .build();
    }

    private LocalDate parseDate(String dateStr) {
        // "20261231" 같은 형식을 LocalDate으로 변환하는 로직 구현
        return null;
    }
}
