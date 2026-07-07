package com.fitu.benefitu.domain.benefits.batch.module;

import javax.swing.text.StyledEditorKit;
import java.util.List;

public interface BenefitsFetcher {
    public boolean hasNextPage();
    public List<RawBenefit>  fetchBenefits(int pageNumber, int pageSize);
}
