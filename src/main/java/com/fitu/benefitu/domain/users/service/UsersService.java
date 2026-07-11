package com.fitu.benefitu.domain.users.service;

import com.fitu.benefitu.domain.auth.SecurityUtil;
import com.fitu.benefitu.domain.benefits.service.BenefitsInnerService;
import com.fitu.benefitu.domain.benefits.types.ResidenceType;
import com.fitu.benefitu.domain.benefits.types.SchoolType;
import com.fitu.benefitu.domain.users.dto.*;
import com.fitu.benefitu.domain.users.entity.Users;
import com.fitu.benefitu.domain.users.entity.UsersDetails;
import com.fitu.benefitu.domain.users.entity.UsersInterests;
import com.fitu.benefitu.domain.users.errors.UsersException;
import com.fitu.benefitu.domain.users.repository.UsersDetailsRepository;
import com.fitu.benefitu.domain.users.repository.UsersInterestsRepository;
import com.fitu.benefitu.domain.users.repository.UsersRepository;
import com.fitu.benefitu.global.error.exception.GeneralException;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UsersService {
    private final UsersRepository usersRepository;
    private final UsersDetailsRepository usersDetailsRepository;
    private final UsersInterestsRepository usersInterestsRepository;
    private final BenefitsInnerService benefitsInnerService;

    public UsersInfoSubmitResponse SubmitInfo(UsersInfoSubmitRequest usersInfoSubmitRequest) {
        Long userId = SecurityUtil.getCurrentUserId();
        Users user = usersRepository.findById(userId).orElseThrow();
        if (user.getHasDetails()) {
            throw new GeneralException(UsersException.ALREADY_INSERTED_CONFLICT);
        }
        System.out.println("[valid] 검증 시작");
        validateSubmitInfo(usersInfoSubmitRequest);

        user.toSubmittedUsers(usersInfoSubmitRequest.baseInfo());

        UsersDetails usersDetails = UsersDetails.createUserDetails(usersInfoSubmitRequest.detailInfo(), user);
        usersDetails.setOwner(user);

        List<UsersInterests> usersInterests = UsersInterests.toInterests(usersInfoSubmitRequest.detailInfo().interests(), user);

        usersDetailsRepository.save(usersDetails);
        usersInterestsRepository.saveAll(usersInterests);

        // 사용자와 혜택 매핑
        usersRepository.flush();
        usersDetailsRepository.flush();
        usersInterestsRepository.flush();
        benefitsInnerService.updateUsersAppliedBenefitsForUser(user);

        List<String> interestsResponses = usersInterests.stream().map(a -> a.getCategory().getDescription()).toList();
        return new UsersInfoSubmitResponse(
                usersInfoSubmitRequest.baseInfo(),
                new DetailInfoResponse(
                        usersDetails.getGpa(),
                        usersDetails.getIncomeBracket(),
                        usersDetails.getIsBasicLiving(),
                        usersDetails.getIsSecondLowest(),
                        interestsResponses
                )
        );
    }

    private void validateSubmitInfo(UsersInfoSubmitRequest usersInfoSubmitRequest) {
        BaseInfoDto baseInfo = usersInfoSubmitRequest.baseInfo();
        var detailInfo = usersInfoSubmitRequest.detailInfo();

        // 1. 학교 검증
        String school = null;
        if (baseInfo.schoolName() == null || !SchoolType.checkSchoolName(baseInfo.schoolName())) {
            school = "존재하지 않거나 누락된 학교 이름입니다.";
        }
        System.out.println("[valid] 학교 검증 완료");

        // 2. 사용자 전공 검증
        String departmentName = null;
        if (baseInfo.department() == null || !SchoolType.checkDepartments(baseInfo.department())) {
            departmentName = "존재하지 않거나 누락된 학과 이름입니다.";
        }
        System.out.println("[valid] 전공 검증 완료");

        // 3. 사용자 학년 검증 (Null Safe)
        String grade = null;
        Integer gradeValue = baseInfo.grade(); // int 대신 Integer로 꺼내기
        if (gradeValue == null || gradeValue > 5 || gradeValue <= 0) {
            grade = "학년은 1에서 5 사이로 입력해주세요.";
        }
        System.out.println("[valid] 학년 검증 완료");

        // 4. 사용자 거주지역 검증
        String residence = null;
        if (baseInfo.residence() == null || !ResidenceType.checkResidenceName(baseInfo.residence())) {
            residence = "존재하지 않거나 누락된 지역입니다.";
        }
        System.out.println("[valid] 거주지역 검증 완료");

        // 5. 사용자 생년월일 검증 (Null Safe)
        String birthday = null;
        LocalDate birthDate = baseInfo.birthDate();
        if (birthDate == null) {
            birthday = "생년월일이 입력되지 않았습니다.";
        } else if (birthDate.isAfter(LocalDate.now())) {
            birthday = "유효한 생년월일이 아닙니다. 미래의 날짜는 입력할 수 없습니다.";
        }
        System.out.println("[valid] 생년월일 검증 완료");

        // 6. 학점 검증 (오타 수정 및 Null Safe)
        String gpa = null;
        Float gpaValue = detailInfo.gpa(); // float 대신 Float로 꺼내기
        if (gpaValue == null || gpaValue > 4.5f || gpaValue < 0.0f) {
            gpa = "학점은 0.0에서 4.5 사이여야 합니다.";
        }
        System.out.println("[valid] 학점 검증 완료");

        // 7. 소득 분위 검증 (Null Safe)
        String incomeBracket = null;
        Integer incomeBracketValue = detailInfo.incomeBracket();
        if (incomeBracketValue == null || incomeBracketValue < 1 || incomeBracketValue > 10) {
            incomeBracket = "소득 분위는 1에서 10 사이여야 합니다.";
        }
        System.out.println("[valid] 소득 분위 검증 완료");

        // 하나라도 에러 메시지가 생성되었다면 묶어서 예외 던지기
        if (school != null || departmentName != null || grade != null
                || residence != null || birthday != null || gpa != null || incomeBracket != null) {

            Result result = new Result(
                    school,
                    departmentName,
                    grade,
                    residence,
                    birthday,
                    gpa,
                    incomeBracket
            );
            throw new GeneralException(UsersException.INVALID_INFO_BAD_REQUEST, result);
        }
    }

    @Builder
    @Getter
    public static class Result {
        String schoolName;
        String department;
        String grade;
        String residence;
        String birthDate;
        String gpa;
        String incomeBracket;
    }

    public UsersInfoSubmitResponse getInfo() {
        Long userId = SecurityUtil.getCurrentUserId();
        Users user = usersRepository.findById(userId).orElseThrow();
        UsersDetails usersDetails = usersDetailsRepository.findByUsers(user);
        List<UsersInterests> usersInterests = usersInterestsRepository.findByUsers(user);
        List<String> interestsResponses = usersInterests.stream().map(a -> a.getCategory().getDescription()).toList();

        return new UsersInfoSubmitResponse(
                new BaseInfoDto(
                        user.getName(),
                        user.getSchoolName(),
                        user.getDepartment(),
                        user.getGrade(),
                        user.getResidence(),
                        user.getBirthDate()
                ),
                new DetailInfoResponse(
                        usersDetails.getGpa(),
                        usersDetails.getIncomeBracket(),
                        usersDetails.getIsBasicLiving(),
                        usersDetails.getIsSecondLowest(),
                        interestsResponses
                )
        );
    }

    public UsersInfoSubmitResponse updateInfo(UsersInfoSubmitRequest usersInfoSubmitRequest) {
        Long userId = SecurityUtil.getCurrentUserId();
        Users user = usersRepository.findById(userId).orElseThrow();
        System.out.println("[valid] 검증 시작");
        validateSubmitInfo(usersInfoSubmitRequest);

        user.toSubmittedUsers(usersInfoSubmitRequest.baseInfo());

        UsersDetails usersDetails = usersDetailsRepository.findByUsers(user);
        usersDetails.updateUserDetails(usersInfoSubmitRequest.detailInfo());

        List<UsersInterests> usersInterests = usersInterestsRepository.findByUsers(user);
        usersInterestsRepository.deleteAll(usersInterests);

        List<UsersInterests> newUsersInterests = UsersInterests.toInterests(usersInfoSubmitRequest.detailInfo().interests(), user);
        usersInterests.addAll(newUsersInterests);

        usersInterestsRepository.saveAll(newUsersInterests);

        // 사용자 혜택 매핑
        usersRepository.flush();
        usersDetailsRepository.flush();
        usersInterestsRepository.flush();
        benefitsInnerService.deleteAllUsersAppliedBenefitsByUsers(user);
        benefitsInnerService.updateUsersAppliedBenefitsForUser(user);

        List<String> interestsResponses = newUsersInterests.stream().map(a -> a.getCategory().getDescription()).toList();
        return new UsersInfoSubmitResponse(
                usersInfoSubmitRequest.baseInfo(),
                new DetailInfoResponse(
                        usersDetails.getGpa(),
                        usersDetails.getIncomeBracket(),
                        usersDetails.getIsBasicLiving(),
                        usersDetails.getIsSecondLowest(),
                        interestsResponses
                )
        );
    }

    public UsersMetadataResponse getMetadata() {
        List<UsersSchoolDto> schoolResponse = new ArrayList<>();
        SchoolType school = SchoolType.INCHEON;
        schoolResponse.add(new UsersSchoolDto(
                school.getSchoolId(),
                school.getSchoolName(),
                school.getDepartments().stream().map(
                        a -> new UsersSchoolDto.Department(
                                a.getDepartmentCode(),
                                a.getDepartmentName()
                        )
                ).toList()
        ));
        return new UsersMetadataResponse(
                schoolResponse,
                Arrays.stream(ResidenceType.values()).map(ResidenceType::getResidenceName).toList()
        );
    }
}
