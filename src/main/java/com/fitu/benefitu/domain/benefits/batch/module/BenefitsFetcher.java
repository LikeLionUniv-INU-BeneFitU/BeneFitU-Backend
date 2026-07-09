package com.fitu.benefitu.domain.benefits.batch.module;

import com.fitu.benefitu.domain.benefits.types.ExtractorId;
import com.fitu.benefitu.domain.benefits.types.ResidenceType;

public interface BenefitsFetcher {
    public boolean hasNextPage(FetchedResults fetchedResults);

    public FetchedResults fetchBenefits(ExtractorId extractorId, ResidenceType residenceType, int pageNumber, int pageSize);
}
