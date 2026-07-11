package com.fitu.benefitu.domain.benefits.service;

import com.fitu.benefitu.domain.auth.service.AuthService;
import com.fitu.benefitu.domain.benefits.dto.GetBenefitListResponse;
import com.fitu.benefitu.domain.benefits.dto.SetApplyStatusResponse;
import com.fitu.benefitu.domain.benefits.entity.Benefits;
import com.fitu.benefitu.domain.benefits.repository.BenefitsRepository;
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
        int pagesize = 3;
        CategoryType categoryType = CategoryType.fromString(category);
        SortType sortType = SortType.valueOf(sort);

        Users user = usersRepository.findById(authService.getUserId()).orElseThrow();
        List<Benefits> usersAppliedBenefitsList = new ArrayList<>(usersAppliedBenefitsRepository.findByUser(user).stream().map(UsersAppliedBenefits::getBenefit).toList());

        if (sortType.equals(SortType.AMOUNT_HIGH)) {
            usersAppliedBenefitsList.sort(Comparator.comparingLong(Benefits::getAmount).reversed());
        } else if (sortType.equals(SortType.DEADLINE_IMMINENT)) {
            usersAppliedBenefitsList.sort(Comparator.comparing(Benefits::getDeadLine, Comparator.nullsLast(Comparator.naturalOrder())));
        }
        List<GetBenefitListResponse.Benefits> benefitsList = new ArrayList<>();
        if ((page + 1) * pagesize <= usersAppliedBenefitsList.size()) {
            for (int i = pagesize * page; i < pagesize * page + pagesize; i++) {
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

        int totalPageNumber = usersAppliedBenefitsList.size() / pagesize - 1;

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
}
