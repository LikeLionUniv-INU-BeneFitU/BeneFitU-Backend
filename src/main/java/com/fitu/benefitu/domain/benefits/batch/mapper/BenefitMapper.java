package com.fitu.benefitu.domain.benefits.batch.mapper;

import com.fitu.benefitu.domain.benefits.batch.module.youthcenter.dto.YouthPolicyApiResponse;
import com.fitu.benefitu.domain.benefits.entity.types.BenefitStatus;
import com.fitu.benefitu.domain.benefits.entity.Benefits;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class BenefitMapper {
    public Benefits toEntity(YouthPolicyApiResponse.YouthPolicy apiData) {
        return Benefits.builder()
                .source_id(apiData.getPlcyNo())
                .benefit_name(apiData.getPlcyNm())
                .benefit_url(apiData.getAplyUrlAddr())
                .status(BenefitStatus.SAFE)
                .fetched_at(LocalDateTime.now())
                // deadLine은 String -> LocalDateTime 파싱 로직 필요
                .deadLine(parseDate(apiData.getBizPrdEndYmd()))
                .build();
    }

    private LocalDateTime parseDate(String dateStr) {
        // "20261231" 같은 형식을 LocalDateTime으로 변환하는 로직 구현
        return null;
    }
}
