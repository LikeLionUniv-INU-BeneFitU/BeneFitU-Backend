package com.fitu.benefitu.domain.users.service;

import com.fitu.benefitu.domain.users.dto.*;
import com.fitu.benefitu.domain.users.entity.UsersDetails;
import com.fitu.benefitu.domain.users.entity.UsersInterests;
import com.fitu.benefitu.domain.users.errors.UsersException;
import com.fitu.benefitu.domain.users.entity.Users;
import com.fitu.benefitu.domain.users.repository.UsersDetailsRepository;
import com.fitu.benefitu.domain.users.repository.UsersInterestsRepository;
import com.fitu.benefitu.domain.users.repository.UsersRepository;
import com.fitu.benefitu.global.error.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UsersService {
    private final UsersRepository usersRepository;

    private final UsersDetailsRepository usersDetailsRepository;
    private final UsersInterestsRepository usersInterestsRepository;

    public UsersSubmitInfoResponse submitInfo(UsersSubmitInfoRequest response, String username) {
        Users user = usersRepository.findByUsername((username));
        //1. 검증

        // 학년 검증(1~5학년 검증) **낮에 나온 의견 보고 수정하기
        if (response.baseInfo().grade() < 1 || response.baseInfo().grade() > 5) {
            throw new GeneralException(UsersException.INVALID_GRADE_BAD_REQUEST);
        }
        // 생년월일 검증(현재 기준 이전 날짜인지)
        if (response.baseInfo().birthDate().isAfter(java.time.LocalDate.now())) {
            throw new GeneralException(UsersException.INVALID_BIRTHDATE_BAD_REQUEST);
        }

        // 학점 검증(0.0~4.5인지)
        if (response.detailInfo().gpa() < 0.0 || response.detailInfo().gpa() > 4.5) {
            throw new GeneralException(UsersException.INVALID_GPA_BAD_REQUEST);
        }

        // 소득분위 검증(1~10 인지)
        if (response.detailInfo().incomeBracket() < 1 || response.detailInfo().incomeBracket() > 10) {
            throw new GeneralException(UsersException.INVALID_INCOME_BAD_REQUEST);
        }
        //2. 저장
        //객체 생성
        UsersDetails usersDetails = UsersDetails.createUsersDetails(response, user);
        UsersInterests usersInterests = UsersInterests.createUsersInterests(response, user);

        //DB 저장
        usersDetailsRepository.save(usersDetails);
        usersInterestsRepository.save(usersInterests);

        //3. 반환

        DetailInfoResponse detailInfoResponse = new DetailInfoResponse(
                usersDetails.getGpa(),
                usersDetails.getIncomeBracket(),
                usersDetails.getIsBasicLiving(),
                usersDetails.getIsSecondLowest(),
                user.getId()
        );

        return new UsersSubmitInfoResponse(response.baseInfo(),detailInfoResponse);

    }

    @Transactional(readOnly = true)
    public UsersInfoResponse getUserInfo(String username) {
        Users user = usersRepository.findByUsername(username);
        if (user == null) {
            throw new GeneralException(UsersException.WRONG_USER_FORM_BAD_REQUEST);
        }

        UsersDetails usersDetails = usersDetailsRepository.findByUserId(user)
                .orElseThrow(() -> new GeneralException(UsersException.WRONG_USER_FORM_BAD_REQUEST));

        List<UsersInterests> interestsList = usersInterestsRepository.findAllByUser(user);


        List<String> interests = interestsList.stream()
                .map(UsersInterests::getCategory)
                .toList();

        BaseInfoResponseDto baseInfo = new BaseInfoResponseDto(
                user.getName(),
                user.getSchoolName(),
                user.getDepartment(),
                user.getGrade(),
                user.getResidence(),
                user.getBirthDate()
        );

        DetailInfoResponse detailInfo = new DetailInfoResponse(
                usersDetails.getGpa(),
                usersDetails.getIncomeBracket(),
                usersDetails.getIsBasicLiving(),
                usersDetails.getIsSecondLowest(),
                user.getId()
        );

        return new UsersInfoResponse(baseInfo, detailInfo);
    }

}
