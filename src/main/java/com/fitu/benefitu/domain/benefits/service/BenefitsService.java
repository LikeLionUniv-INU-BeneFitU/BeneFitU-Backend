package com.fitu.benefitu.domain.benefits.service;

import com.fitu.benefitu.domain.auth.service.AuthService;
import com.fitu.benefitu.domain.benefits.dto.*;
import com.fitu.benefitu.domain.benefits.entity.BenefitNotes;
import com.fitu.benefitu.domain.benefits.entity.Benefits;
import com.fitu.benefitu.domain.benefits.errors.BenefitsException;
import com.fitu.benefitu.domain.benefits.repository.BenefitsRepository;
import com.fitu.benefitu.domain.benefits.types.BenefitCategory;
import com.fitu.benefitu.domain.users.entity.Users;
import com.fitu.benefitu.domain.users.entity.UsersAppliedBenefits;
import com.fitu.benefitu.domain.users.entity.UsersDetails;
import com.fitu.benefitu.domain.users.errors.UsersException;
import com.fitu.benefitu.domain.users.repository.UsersAppliedBenefitsRepository;
import com.fitu.benefitu.domain.users.repository.UsersDetailsRepository;
import com.fitu.benefitu.domain.users.repository.UsersRepository;
import com.fitu.benefitu.domain.users.type.ApplyStatus;
import com.fitu.benefitu.domain.users.type.CategoryType;
import com.fitu.benefitu.domain.users.type.SortType;
import com.fitu.benefitu.global.error.exception.GeneralException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
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
    private final UsersDetailsRepository userDetailsRepository;
    private final UsersAppliedBenefitsRepository usersAppliedBenefitsRepository;
    private final BenefitsRepository benefitsRepository;

    public GetBenefitListResponse getBenefitList(String category, String sort, Integer page) {
        int pageSize = 10;
        CategoryType categoryType = CategoryType.fromString(category);
        SortType sortType = SortType.valueOf(sort);

        Users user = usersRepository.findById(authService.getUserId()).orElseThrow();
        List<Benefits> usersAppliedBenefitsList = new ArrayList<>(
                usersAppliedBenefitsRepository.findByUserAndStatus(user, ApplyStatus.NOT_APPLIED)
                        .stream()
                        .map(UsersAppliedBenefits::getBenefit)
                        .toList()
        );

        // 1. 카테고리 필터링이 있다면 '정렬과 페이징 전'에 미리 수행해서 데이터 유실을 방지합니다.
        if (!categoryType.equals(CategoryType.ALL)) {
            usersAppliedBenefitsList = usersAppliedBenefitsList.stream()
                    .filter(benefits -> benefits.getCategories().stream()
                            .anyMatch(a -> a.getBenefitCategory().mappingCategoryTreeAndReturnDescription(categoryType) != null))
                    .toList();
        }

        // 2. 정렬 (Null 세이프하게 다듬기)
        if (sortType.equals(SortType.AMOUNT_HIGH)) {
            usersAppliedBenefitsList.sort(Comparator.comparing(Benefits::getAmount, Comparator.nullsLast(Comparator.reverseOrder())));
        } else if (sortType.equals(SortType.DEADLINE_IMMINENT)) {
            usersAppliedBenefitsList.sort(Comparator.comparing(Benefits::getDeadLine, Comparator.nullsLast(Comparator.naturalOrder())));
        }

        // 3. 안전한 총 페이지 수 산출 (데이터가 0개면 0페이지, 13개면 2페이지 등 올림 처리)
        int totalElements = usersAppliedBenefitsList.size();
        int totalPageNumber = (totalElements == 0) ? 0 : (int) Math.ceil((double) totalElements / pageSize);

        // 4. 안전한 페이징 인덱스 계산 (데이터 개수가 부족해도 에러가 나지 않음)
        int fromIndex = page * pageSize;

        // 만약 프론트가 전체 데이터 수보다 더 큰 페이지 번호를 요청하면 빈 리스트 리턴
        if (fromIndex >= totalElements) {
            return new GetBenefitListResponse(totalPageNumber, List.of());
        }

        // 끝점은 데이터 크기를 넘지 않도록 Math.min으로 방어
        int toIndex = Math.min(fromIndex + pageSize, totalElements);

        // 5. 필요한 구간만 안전하게 잘라서(subList) DTO로 변환
        List<GetBenefitListResponse.Benefits> benefitsList = usersAppliedBenefitsList.subList(fromIndex, toIndex).stream()
                .map(benefits -> {
                    String amount = "조건별 상이함";
                    if (benefits.getAmount() != null && benefits.getAmount() / 10000 > 0) {
                        amount = "최대 " + benefits.getAmount() / 10000 + " 만원";
                    }

                    String dDay = (benefits.getDeadLine() != null)
                            ? "D - " + ChronoUnit.DAYS.between(LocalDate.now(), benefits.getDeadLine())
                            : "상시모집";

                    return new GetBenefitListResponse.Benefits(
                            benefits.getId(),
                            benefits.getBenefitName(),
                            benefits.getCategories().stream().map(a -> a.getBenefitCategory().getDescription()).toList(),
                            dDay,
                            amount
                    );
                })
                .toList();

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
        List<UsersAppliedBenefits> appliedBenefits = usersAppliedBenefitsRepository.findByUserAndStatus(user, ApplyStatus.NOT_APPLIED);
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

    public GetAppliedBenefitsResponse getAppliedBenefits(int pageNumber) {
        int pageSize = 10;
        Users user = usersRepository.findById(authService.getUserId()).orElseThrow();

        // 1. 조건에 맞는 데이터만 필터링 및 변환 진행
        List<GetAppliedBenefitsResponse.AppliedBenefits> appliedBenefitsList = usersAppliedBenefitsRepository.findByUser(user)
                .stream()
                .filter(a -> !a.getStatus().equals(ApplyStatus.NOT_APPLIED))
                .map(a -> new GetAppliedBenefitsResponse.AppliedBenefits(
                        a.getBenefit().getId(),
                        a.getBenefit().getBenefitName(),
                        a.getAppliedAt().toString(),
                        a.getStatus().toString()
                ))
                .toList();

        // 2. 필터링이 완전히 완료된 '최종 데이터 개수'로 페이지 수를 산출해야 합니다.
        int totalElements = appliedBenefitsList.size();
        int totalPageNumber = (totalElements == 0) ? 0 : (int) Math.ceil((double) totalElements / pageSize);

        // 3. 안전한 페이징 구간 계산 (시작점과 끝점 구하기)
        int fromIndex = pageNumber * pageSize;

        // 요청한 페이지 번호가 전체 데이터 범위를 벗어나면 바로 빈 리스트 반환
        if (fromIndex >= totalElements) {
            return new GetAppliedBenefitsResponse(totalPageNumber, List.of());
        }

        int toIndex = Math.min(fromIndex + pageSize, totalElements);

        // 4. 안전하게 서브리스트로 잘라서 반환
        List<GetAppliedBenefitsResponse.AppliedBenefits> pagedList = appliedBenefitsList.subList(fromIndex, toIndex);

        return new GetAppliedBenefitsResponse(totalPageNumber, pagedList);
    }

    public GetBenefitsDetailsResponse getBenefitsDetails(Long benefitId) {
        Users user = usersRepository.findById(authService.getUserId()).orElseThrow();
        Benefits benefits = benefitsRepository.findById(benefitId).orElseThrow();
        if(!usersAppliedBenefitsRepository.existsByUserAndBenefit(user, benefits)) {
            throw new GeneralException(BenefitsException.INTERNAL_SERVER_ERROR);
        }
        UsersAppliedBenefits userAppliedBenefits = usersAppliedBenefitsRepository.findByUserAndBenefit(user, benefits);

        boolean hasDetails = benefits.getNotes().isEmpty();
        String amount = "조건별 상이함";
        if(benefits.getAmount() > 0){
            amount = String.format("%,d", benefits.getAmount())+ "원";
        }

        List<String> notes = new ArrayList<>();
        if(hasDetails) {
            List<String> list = new ArrayList<>();
            for (BenefitNotes a : benefits.getNotes()) {
                if (a.getNote().length() > 5) {
                    String note = a.getNote();
                    list.add(note);
                }
            }
            notes = list;
        }

        GetBenefitsDetailsResponse.BenefitsDetails benefitsDetails = new GetBenefitsDetailsResponse.BenefitsDetails(
                benefitId,
                benefits.getBenefitName(),
                benefits.getCategories().getFirst().getBenefitCategory().getDescription(),
                amount,
                benefits.getDeadLine().toString(),
                benefits.getBenefitUrl(),
                notes
        );

        GetBenefitsDetailsResponse.MatchedConditions matchedConditions = new GetBenefitsDetailsResponse.MatchedConditions(
                "학점 " + benefits.getScoringWeights().getGpa() + " 이상",
                "소득분위 " + (benefits.getScoringWeights().getIncomeBracket()==null?10:benefits.getScoringWeights().getIncomeBracket())+"구간 이하",
                "기초생활수급자" + (benefits.getScoringWeights().getIsBasicLiving()?"이" : " 상관없이"),
                "차상위계층" + (benefits.getScoringWeights().getIsSecondLowest()?"이":" 상관없이")
        );

        int passProbability = 100;
        int machedCount = 0;
        UsersDetails usersDetails = userDetailsRepository.findByUsers(user);
        if(usersDetails.getGpa() >= benefits.getScoringWeights().getGpa()){
            machedCount++;
        }
        System.out.println("[매칭 수] "+machedCount);
        if(benefits.getScoringWeights().getIncomeBracket() == null){
            machedCount++;
        }else  if(usersDetails.getIncomeBracket() <= benefits.getScoringWeights().getIncomeBracket()){
            machedCount++;
        }
        System.out.println("[매칭 수] "+machedCount);
        if(!benefits.getScoringWeights().getIsBasicLiving()){
            machedCount++;
        }
        System.out.println("[매칭 수] "+machedCount);
        if(!benefits.getScoringWeights().getIsSecondLowest()){
            machedCount++;
        }
        System.out.println("[매칭 수] "+machedCount);

        passProbability = 25*machedCount;

        return new GetBenefitsDetailsResponse(
                hasDetails,
                benefitsDetails,
                matchedConditions,
                passProbability + "%"
        );
    }
}
