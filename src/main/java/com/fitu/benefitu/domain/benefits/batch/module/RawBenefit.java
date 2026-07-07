package com.fitu.benefitu.domain.benefits.batch.module;

import com.fitu.benefitu.domain.benefits.types.ExtractorId;

public record RawBenefit(
        // 추출기 번호
        ExtractorId sourceId
) {
}
