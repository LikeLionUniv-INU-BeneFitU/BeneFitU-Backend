package com.fitu.benefitu.domain.users.service;

import com.fitu.benefitu.domain.users.dto.*;
import com.fitu.benefitu.domain.users.entity.UsersDetails;
import com.fitu.benefitu.domain.users.entity.UsersInterests;
import com.fitu.benefitu.domain.users.errors.AuthException;
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

    public AuthSignupResponse signup(AuthSignupRequest request) {
        //1. 검증
        checkUsernameExists(request.username());
        checkUserIdAndPassword(request.username(), request.password());

        //2. 저장
        //Users 객체 생성
        Users user = Users.createUsers(request);
        //DB 저장
        usersRepository.save(user);

        //3. 반환
        return new AuthSignupResponse(user.getUsername());
    }

    // 중복된 사용자에 대한 검증 (수정됨: null이 아닐 때 에러)
    private void checkUsernameExists(String username) {
        Users user = usersRepository.findByUsername(username);
        if (user != null) { // 유저가 존재하면 중복이므로 에러!
            throw new GeneralException(AuthException.ALREADY_EXIST_USER_ID_BAD_REQUEST);
        }
    }

    // ID/PW 형식 검증
    private void checkUserIdAndPassword(String username, String password) {
        // 영문, 숫자, 특수문자를 포함한 8자 이상 정규표현식
        String passwordPattern = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$";

        // ID 검증 (빈 문자열 체크)
        if (username == null || username.trim().isEmpty()) {
            throw new GeneralException(AuthException.WRONG_USER_FORM_BAD_REQUEST);
        }

        // PW 검증 (길이 + 정규식 체크)
        if (password == null || !password.matches(passwordPattern)) {
            throw new GeneralException(AuthException.WRONG_USER_FORM_BAD_REQUEST);
        }
    }

    public UsersSubmitInfoResponse submitInfo(UsersSubmitInfoRequest response, String username) {
        Users user = usersRepository.findByUsername((username));
        //1. 검증

        // 학년 검증(1~5학년 검증) **낮에 나온 의견 보고 수정하기
        if (response.baseInfo().grade() < 1 || response.baseInfo().grade() > 5) {
            throw new GeneralException(AuthException.INVALID_GRADE_BAD_REQUEST);
        }
        // 생년월일 검증(현재 기준 이전 날짜인지)
        if (response.baseInfo().birthDate().isAfter(java.time.LocalDate.now())) {
            throw new GeneralException(AuthException.INVALID_BIRTHDATE_BAD_REQUEST);
        }

        // 학점 검증(0.0~4.5인지)
        if (response.detailInfo().gpa() < 0.0 || response.detailInfo().gpa() > 4.5) {
            throw new GeneralException(AuthException.INVALID_GPA_BAD_REQUEST);
        }

        // 소득분위 검증(1~10 인지)
        if (response.detailInfo().incomeBracket() < 1 || response.detailInfo().incomeBracket() > 10) {
            throw new GeneralException(AuthException.INVALID_INCOME_BAD_REQUEST);
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
            throw new GeneralException(AuthException.WRONG_USER_FORM_BAD_REQUEST);
        }

        UsersDetails usersDetails = usersDetailsRepository.findByUserId(user)
                .orElseThrow(() -> new GeneralException(AuthException.WRONG_USER_FORM_BAD_REQUEST));

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
