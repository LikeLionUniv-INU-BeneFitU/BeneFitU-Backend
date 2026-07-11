package com.fitu.benefitu.domain.benefits.controller;

import com.fitu.benefitu.domain.benefits.dto.GetBenefitListResponse;
import com.fitu.benefitu.domain.benefits.service.BenefitsService;
import com.fitu.benefitu.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/benefits")
public class BenefitController {
    private final BenefitsService benefitsService;

    @GetMapping
    public ResponseEntity<ApiResponse<GetBenefitListResponse>> getBenefits(
            // required=false를 주어 미지정 시 기본값(default)을 적용할 수 있게 합니다.
            @RequestParam(value = "category", defaultValue = "ALL") String category,
            @RequestParam(value = "sort", defaultValue = "DEFAULT") String sort,
            // 0 부터 시작
            @RequestParam(value = "page", defaultValue = "10") Integer page
    ){
        GetBenefitListResponse response = benefitsService.getBenefitList(category, sort, page);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
