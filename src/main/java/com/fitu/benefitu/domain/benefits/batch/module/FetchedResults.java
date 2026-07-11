package com.fitu.benefitu.domain.benefits.batch.module;

import com.fitu.benefitu.domain.benefits.types.ExtractorId;

import java.util.List;

public record FetchedResults(
        // 추출기 번호
        ExtractorId sourceId,
        // 전체 개수
        int totCount,
        // page 번호
        int pageNum,
        // page 크기
        int pageSize,

        List<RawBenefit> benefits
) {
}
