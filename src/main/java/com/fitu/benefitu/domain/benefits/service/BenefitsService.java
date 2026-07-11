package com.fitu.benefitu.domain.benefits.service;

import com.fitu.benefitu.domain.auth.service.AuthService;
import com.fitu.benefitu.domain.benefits.dto.*;
import com.fitu.benefitu.domain.benefits.entity.Benefits;
import com.fitu.benefitu.domain.benefits.repository.BenefitsRepository;
import com.fitu.benefitu.domain.benefits.types.BenefitCategory;
import com.fitu.benefitu.domain.users.entity.Users;
import com.fitu.benefitu.domain.users.entity.UsersAppliedBenefits;
import com.fitu.benefitu.domain.users.repository.UsersAppliedBenefitsRepository;
import com.fitu.benefitu.domain.users.repository.UsersRepository;
import com.fitu.benefitu.domain.users.type.ApplyStatus;
import com.fitu.benefitu.domain.users.type.CategoryType;
import com.fitu.benefitu.domain.users.type.SortType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BenefitsService {
    private final AuthService authService;
    private final UsersRepository usersRepository;
    private final UsersAppliedBenefitsRepository usersAppliedBenefitsRepository;
    private final BenefitsRepository benefitsRepository;

    public GetBenefitListResponse getBenefitList(String category, String sort, Integer page) {
        int pageSize = 3;
        CategoryType categoryType = CategoryType.fromString(category);
        SortType sortType = SortType.valueOf(sort);

        Users user = usersRepository.findById(authService.getUserId()).orElseThrow();
        List<Benefits> usersAppliedBenefitsList = new ArrayList<>(usersAppliedBenefitsRepository.findByUserAndStatus(user, ApplyStatus.NOT_APPLIED).stream().map(UsersAppliedBenefits::getBenefit).toList());

        if (sortType.equals(SortType.AMOUNT_HIGH)) {
            usersAppliedBenefitsList.sort(Comparator.comparingLong(Benefits::getAmount).reversed());
        } else if (sortType.equals(SortType.DEADLINE_IMMINENT)) {
            usersAppliedBenefitsList.sort(Comparator.comparing(Benefits::getDeadLine, Comparator.nullsLast(Comparator.naturalOrder())));
        }
        List<GetBenefitListResponse.Benefits> benefitsList = new ArrayList<>();
        if ((page + 1) * pageSize <= usersAppliedBenefitsList.size()) {
            for (int i = pageSize * page; i < pageSize * page + pageSize; i++) {
                Benefits benefits = usersAppliedBenefitsList.get(i);
                if (!categoryType.equals(CategoryType.ALL)) {
                    if (benefits.getCategories().stream()
                            .noneMatch(a ->
                                    a.getBenefitCategory()
                                            .mappingCategoryTreeAndReturnDescription(categoryType) != null)) {
                        continue;
                    }
                }

                String amount = "조건별 상이함";
                if (benefits.getAmount() != null) {
                    if (benefits.getAmount() / 10000 > 0) {
                        amount = "최대 " + benefits.getAmount() / 10000 + " 만원";
                    }
                }
                benefitsList.add(new GetBenefitListResponse.Benefits(
                        benefits.getId(),
                        benefits.getBenefitName(),
                        benefits.getCategories().stream().map(a -> a.getBenefitCategory().getDescription()).toList(),
                        "D - " + ChronoUnit.DAYS.between(LocalDate.now(), benefits.getDeadLine()),
                        amount
                ));
            }
        }

        int totalPageNumber = usersAppliedBenefitsList.size() / pageSize - 1;

        return new GetBenefitListResponse(totalPageNumber, benefitsList);
    }

    public SetApplyStatusResponse getApplyStatus(String benefitId, String status) {
        Users user = usersRepository.findById(authService.getUserId()).orElseThrow();
        Benefits benefits = benefitsRepository.findById(Long.parseLong(benefitId)).orElseThrow();
        ApplyStatus applyStatus = ApplyStatus.valueOf(status);

        UsersAppliedBenefits appliedbenefits = usersAppliedBenefitsRepository.findByUserAndBenefit(user, benefits);

        appliedbenefits.updateApplyStatus(applyStatus);

        return new SetApplyStatusResponse(benefits.getId());
    }

    public CountByCategoryResponse getCountByCategory() {
        Users user = usersRepository.findById(authService.getUserId()).orElseThrow();
        List<UsersAppliedBenefits> appliedBenefits = usersAppliedBenefitsRepository.findByUserAndStatus(user, ApplyStatus.NOT_SELECTED);
        int corporateCount = 0;
        int regionCount = 0;
        int requirementsCount = 0;
        int stateCount = 0;

        for(UsersAppliedBenefits benefits : appliedBenefits){
            BenefitCategory category = benefits.getBenefit().getCategories().getFirst().getBenefitCategory();
            if(category.equals(BenefitCategory.CORPORATE)){
                corporateCount++;
            }
            else if(category.equals(BenefitCategory.REGIONAL)){
                regionCount++;
            }
            else if(category.equals(BenefitCategory.CONDITIONAL)){
                requirementsCount++;
            }
            else if(category.equals(BenefitCategory.NATIONAL)){
                stateCount++;
            }
        }

        return new CountByCategoryResponse(
                corporateCount,
                regionCount,
                requirementsCount,
                stateCount
        );
    }

    public GetTotalAmountResponse getTotalAmount() {
        Users user = usersRepository.findById(authService.getUserId()).orElseThrow();

        List<UsersAppliedBenefits> appliedBenefits = usersAppliedBenefitsRepository.findByUser(user);
        Long totalAmount = appliedBenefits.stream().mapToLong(
                benefits -> benefits.getBenefit().getAmount()
        ).sum();

        String formattedAmount = String.format("%,d", totalAmount)+ "원";

        return new GetTotalAmountResponse(formattedAmount);
    }

    public GetAppliedBenefitsResponse getAppliedBenefits() {
        Users user = usersRepository.findById(authService.getUserId()).orElseThrow();
        List<UsersAppliedBenefits> appliedBenefits = usersAppliedBenefitsRepository.findByUser(user);
        List<GetAppliedBenefitsResponse.AppliedBenefits> appliedBenefitsList =
                appliedBenefits.stream()
                        .filter(a->!a.getStatus().equals(ApplyStatus.NOT_APPLIED))
                        .map(a->new GetAppliedBenefitsResponse.AppliedBenefits(
                                a.getBenefit().getId(),
                                a.getBenefit().getBenefitName(),
                                a.getAppliedAt().toString(),
                                a.getStatus().toString()
                        )).toList();
        return new GetAppliedBenefitsResponse(appliedBenefitsList);
    }
}
