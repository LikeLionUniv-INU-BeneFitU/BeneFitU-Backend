package com.fitu.benefitu.domain.benefits.batch.module.youthcenter.extractor;

import com.fitu.benefitu.domain.benefits.batch.module.BenefitCategoryExtractor;
import com.fitu.benefitu.domain.benefits.batch.module.RawBenefit;
import org.springframework.stereotype.Component;

@Component
public class YouthCenterBenefitsCategoryExtractor implements BenefitCategoryExtractor {

    // 키워드 조합을 위한 헬퍼 메서드
    private String getCombinedText(RawBenefit raw) {
        return (raw.BenefitName() + " " + raw.contents()).toLowerCase();
    }

    @Override
    public Boolean isNational(RawBenefit rawBenefit) {
        // 코드 매칭 우선
        if ("0054001".equals(rawBenefit.pvsnInstGroupCode())) return true;
        // 키워드 매칭 (코드 없을 때)
        String text = getCombinedText(rawBenefit);
        return text.contains("국가") || text.contains("한국장학재단");
    }

    @Override
    public Boolean isCorporate(RawBenefit rawBenefit) {
        String text = getCombinedText(rawBenefit);
        return text.contains("재단") || text.contains("장학회") || text.contains("기업")
                || text.contains("복지재단") || text.contains("문화재단") || text.contains("사회공헌");
    }

    @Override
    public Boolean isRegional(RawBenefit rawBenefit) {
        // 코드 매칭 우선
        if ("0054002".equals(rawBenefit.pvsnInstGroupCode())) return true;
        // 키워드 매칭
        String text = getCombinedText(rawBenefit);
        return text.contains("시청") || text.contains("도청") || text.contains("군청")
                || text.contains("시립") || text.contains("도립") || text.contains("지자체");
    }

    @Override
    public Boolean isConditional(RawBenefit rawBenefit) {
        // 위 3가지에 해당하지 않지만 장학/학비/등록금 관련 키워드가 있는 경우
        String text = getCombinedText(rawBenefit);
        boolean isScholarshipRelated = text.contains("장학") || text.contains("학비") || text.contains("등록금");

        // 국가지원, 지역지원, 기업지원이 모두 아닌 경우 true
        return isScholarshipRelated
                && !Boolean.TRUE.equals(isNational(rawBenefit))
                && !Boolean.TRUE.equals(isRegional(rawBenefit))
                && !Boolean.TRUE.equals(isCorporate(rawBenefit));
    }
}