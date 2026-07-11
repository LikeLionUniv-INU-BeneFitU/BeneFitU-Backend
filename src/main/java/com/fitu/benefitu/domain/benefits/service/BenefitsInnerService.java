package com.fitu.benefitu.domain.benefits.service;

import com.fitu.benefitu.domain.benefits.entity.BenefitTargetConditions;
import com.fitu.benefitu.domain.benefits.entity.Benefits;
import com.fitu.benefitu.domain.benefits.repository.BenefitTargetConditionsRepository;
import com.fitu.benefitu.domain.benefits.types.ResidenceType;
import com.fitu.benefitu.domain.benefits.types.SchoolType;
import com.fitu.benefitu.domain.users.entity.Users;
import com.fitu.benefitu.domain.users.entity.UsersAppliedBenefits;
import com.fitu.benefitu.domain.users.errors.UsersException;
import com.fitu.benefitu.domain.users.repository.UsersAppliedBenefitsRepository;
import com.fitu.benefitu.domain.users.type.ApplyStatus;
import com.fitu.benefitu.global.error.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class BenefitsInnerService {

    private final UsersAppliedBenefitsRepository usersAppliedBenefitsRepository;
    private final BenefitTargetConditionsRepository benefitTargetConditionsRepository;

    /**
     * UsersAppliedBenefits -> Benefits
     */
    public List<Benefits> getBenefitsByUsersAppliedBenefits(List<UsersAppliedBenefits> usersAppliedBenefits, ApplyStatus applyStatus) {
        return usersAppliedBenefits.stream().filter(a -> a.getStatus().equals(applyStatus)).map(UsersAppliedBenefits::getBenefit).toList();
    }

    /**
     * 배치 혹은 전체 업데이트 시 사용 (N+1 쿼리 최적화 버전)
     **/
    public void UpdateAllUsersAppliedBenefits(List<Users> allUsers, List<Benefits> allBenefits) {
        List<UsersAppliedBenefits> usersAppliedBenefitsList = new ArrayList<>();

        // 1. 루프 밖에서 전체 TargetConditions를 한 번에 조회하여 Map으로 캐싱 (N+1 방지)
        List<BenefitTargetConditions> allTargets = benefitTargetConditionsRepository.findAll();
        Map<Benefits, BenefitTargetConditions> targetMap = allTargets.stream()
                .collect(Collectors.toMap(BenefitTargetConditions::getBenefit, target -> target, (a, b) -> a));

        for (Users user : allUsers) {
            for (Benefits benefit : allBenefits) {
                // DB 조회 대신 미리 준비한 Map 메모리에서 쏙 꺼내오기
                BenefitTargetConditions target = targetMap.get(benefit);

                if (target == null || !canApplyBenefits(user, target)) {
                    continue;
                }

                // ⚠️ 주의: 데이터 정합성상 중복 체크가 필요하지만, 이 역시 N+1 여지가 있으므로
                // 전체 신청 목록을 상단에서 캐싱해서 비교하거나, DB 제약조건(Unique Key)에 의존하고
                // 루프 내 쿼리를 최소화하는 방향을 추천합니다. 일단 정석 체크 유지:
                if (usersAppliedBenefitsRepository.existsByUserAndBenefit(user, benefit)) {
                    continue;
                }

                UsersAppliedBenefits appliedBenefit = new UsersAppliedBenefits(
                        user,
                        benefit,
                        LocalDate.now(),
                        ApplyStatus.NOT_SELECTED
                );
                usersAppliedBenefitsList.add(appliedBenefit);
            }
        }

        usersAppliedBenefitsRepository.saveAll(usersAppliedBenefitsList);
    }

    /**
     * 사용자가 직접 특정 혜택 하나를 신청할 때 사용하는 단건 메서드
     **/
    public UsersAppliedBenefits createUsersAppliedBenefits(Users users, Benefits benefits) {
        BenefitTargetConditions target = benefitTargetConditionsRepository.findByBenefit(benefits);

        if (target == null || !canApplyBenefits(users, target)) {
            throw new GeneralException(UsersException.INVALID_INFO_BAD_REQUEST);
        }

        if (usersAppliedBenefitsRepository.existsByUserAndBenefit(users, benefits)) {
            throw new GeneralException(UsersException.ALREADY_INSERTED_CONFLICT);
        }

        UsersAppliedBenefits usersAppliedBenefits = new UsersAppliedBenefits(
                users,
                benefits,
                LocalDate.now(),
                ApplyStatus.NOT_SELECTED
        );

        return usersAppliedBenefitsRepository.save(usersAppliedBenefits);
    }

    // 💡 단건 루프 딜리트 대신 deleteAllInBatch를 사용하여 단 한 번의 쿼리로 삭제 성능 업그레이드
    public void DeleteAllUsersAppliedBenefitsByUsers(Users users) {
        List<UsersAppliedBenefits> usersAppliedBenefits = usersAppliedBenefitsRepository.findByUser(users);
        usersAppliedBenefitsRepository.deleteAllInBatch(usersAppliedBenefits);
    }

    public void DeleteAllUsersAppliedBenefitsByBenefits(Benefits benefits) {
        List<UsersAppliedBenefits> usersAppliedBenefits = usersAppliedBenefitsRepository.findByBenefit(benefits);
        usersAppliedBenefitsRepository.deleteAllInBatch(usersAppliedBenefits);
    }

    private boolean canApplyBenefits(Users users, BenefitTargetConditions target) {
        // 학교 검증
        SchoolType userSchool = SchoolType.getSchoolTypeBySchoolName(users.getSchoolName());
        if (userSchool == null) return false;

        if (!target.getSchoolType().equals(SchoolType.STANDARD)) {
            if (!userSchool.equals(target.getSchoolType())) return false;
        }

        // 학과 검증
        SchoolType.Department userDepartment = SchoolType.getDepartmentByDepartmentName(users.getDepartment());
        if (userDepartment == null) return false;

        if (!target.getDepartmentType().equals(SchoolType.STANDARD.getDepartmentByCode("0011009"))) {
            if (!userDepartment.equals(target.getDepartmentType())) return false;
        }

        // 학년 검증
        if (target.getGrade() > users.getGrade()) return false;

        // 나이(생년월일) 검증
        LocalDate maxBirthDay = LocalDate.now().minusYears(target.getMinAge());
        LocalDate minBirthDay = LocalDate.now().minusYears(target.getMaxAge() + 1).plusDays(1);

        if (users.getBirthDate().isBefore(minBirthDay) || users.getBirthDate().isAfter(maxBirthDay)) {
            return false;
        }

        // 거주지역 검증
        ResidenceType usersResidence = ResidenceType.getResidenceTypeByResidenceName(users.getResidence());
        if (usersResidence == null) return false;

        if (!target.getResidenceType().equals(usersResidence)) return false;

        // 모든 가드 조건(Guard Clause)을 무사히 통과했다면 true
        return true;
    }
}