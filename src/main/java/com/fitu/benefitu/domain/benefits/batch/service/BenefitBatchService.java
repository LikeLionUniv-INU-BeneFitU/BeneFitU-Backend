package com.fitu.benefitu.domain.benefits.batch.service;

import com.fitu.benefitu.domain.benefits.batch.engine.ExtractEngine;
import com.fitu.benefitu.domain.benefits.batch.engine.FetchEngine;
import com.fitu.benefitu.domain.benefits.entity.Benefits;
import com.fitu.benefitu.domain.benefits.repository.BenefitsRepository;
import jakarta.transaction.Transactional;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class BenefitBatchService {
    private final FetchEngine fetchEngine;
    private final ExtractEngine extractEngine;
    private final BenefitsRepository benefitsRepository;

    public Boolean fetchAndSaveBenefits(){
        // 처리한 전체 페이지 수
        int totalCount = 0;
        // 저장한 혜택 수
        int savedCount = 0;
        // 에러 처리된 혜택 수
        int errorCount = 0;

        // 페이지가 있을 경우에만
        while (true) {
            // 1. 수집 : API에서 데이터 가져옴
//            var rawList = fetchEngine;
            // 2. 가공 : 스트림으로 바로 변환
//            List<Benefits> entities = rawList.stream()
//                    .map(this::extractEngine)
//                    .collect(Collectors.toList());
            // 3. 저장 : DB로 저장
//            benefitsRepository.saveAll(entities);
        }
    }
}
