package com.fitu.benefitu.domain.benefits.batch.module.youthcenter.extractor;

import com.fitu.benefitu.domain.benefits.batch.module.BenefitsScoringWeightsExtractor;
import com.fitu.benefitu.domain.benefits.batch.module.RawBenefit;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class YouthCenterBenefitsScoringWeightsExtractor implements BenefitsScoringWeightsExtractor {
    // 숫자(분위/구간/등급) + (이하/미만/이내)를 찾는 패턴
    private static final Pattern INCOME_LIMIT_PATTERN = Pattern.compile("(\\d+)\\s*(분위|구간|등급)\\s*(이하|이내)");
    @Override
    public Float extractGpa(RawBenefit rawBenefit) {
        return 0f;
    }

    @Override
    public Integer extractIncomeBracket(RawBenefit rawBenefit) {
        String text = rawBenefit.etcList(); // 기타 사항(etcList)에는 소득 정보가 상세히 기술됨
        if (text == null || text.isBlank()) return null;

        Matcher matcher = INCOME_LIMIT_PATTERN.matcher(text);

        if (matcher.find()) {
            // 매칭된 숫자(그룹 1)를 바로 Integer로 변환
            return Integer.parseInt(matcher.group(1));
        }

        return null; // 정보가 없으면 null 반환
    }

    @Override
    public Boolean isBasicLiving(RawBenefit rawBenefit) {
        String text = rawBenefit.etcList();
        // 이미 dto에서 기본적으로 체크된 게 있다면 우선, 없으면 키워드 검색
        return rawBenefit.isBasicLiving() ||
                text.contains("기초생활수급자") ||
                text.contains("기초수급자");
    }

    @Override
    public Boolean isSecondLowest(RawBenefit rawBenefit) {
        String text = rawBenefit.etcList();
        return text.contains("차상위계층") ||
                text.contains("차상위");
    }
}
