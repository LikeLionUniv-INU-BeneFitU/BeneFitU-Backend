package com.fitu.benefitu.domain.benefits.controller;

import com.fitu.benefitu.domain.benefits.dto.*;
import com.fitu.benefitu.domain.benefits.service.BenefitsService;
import com.fitu.benefitu.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            @RequestParam(value = "page", defaultValue = "0") Integer page
    ) {
        GetBenefitListResponse response = benefitsService.getBenefitList(category, sort, page);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/count-by-category")
    public ResponseEntity<ApiResponse<CountByCategoryResponse>> getBenefitsCountByCategory(){
        return ResponseEntity.ok(ApiResponse.success(benefitsService.getCountByCategory()));
    }

    @GetMapping("/total-amount")
    public ResponseEntity<ApiResponse<GetTotalAmountResponse>> getBenefitsTotalAmount(){
        return ResponseEntity.ok(ApiResponse.success(benefitsService.getTotalAmount()));
    }

    @GetMapping("/applied")
    public ResponseEntity<ApiResponse<GetAppliedBenefitsResponse>> getAppliedBenefits(){
        return ResponseEntity.ok(ApiResponse.success(benefitsService.getAppliedBenefits()));
    }

    @PostMapping("/{benefitId}/apply")
    public ResponseEntity<ApiResponse<SetApplyStatusResponse>> applyBenefit(
            @PathVariable String benefitId,
            @RequestBody SetApplyStatusRequest request
    ) {
        return ResponseEntity.ok(ApiResponse.success(benefitsService.getApplyStatus(benefitId, request.applyStatus())));
    }
}
