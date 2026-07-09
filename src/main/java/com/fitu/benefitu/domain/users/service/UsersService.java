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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UsersService {
    private final UsersRepository usersRepository;
    private final UsersDetailsRepository usersDetailsRepository;
    private final UsersInterestsRepository usersInterestsRepository;

    public AuthSignupResponse signup(AuthSignupRequest request) {
        checkUsernameExists(request.username());
        checkUserIdAndPassword(request.username(), request.password());

        Users user = Users.createUsers(request);
        usersRepository.save(user);

        return new AuthSignupResponse(user.getUsername());
    }

    private void checkUsernameExists(String username) {
        Users user = usersRepository.findByUsername(username);
        if (user != null) {
            throw new GeneralException(AuthException.ALREADY_EXIST_USER_ID_BAD_REQUEST);
        }
    }

    private void checkUserIdAndPassword(String username, String password) {
        String passwordPattern = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$";

        if (username == null || username.trim().isEmpty()) {
            throw new GeneralException(AuthException.WRONG_USER_FORM_BAD_REQUEST);
        }

        if (password == null || !password.matches(passwordPattern)) {
            throw new GeneralException(AuthException.WRONG_USER_FORM_BAD_REQUEST);
        }
    }

    public UsersSubmitInfoResponse submitInfo(UsersSubmitInfoRequest response, String username) {
        Users user = usersRepository.findByUsername(username);

        if (response.baseInfo().grade() < 1 || response.baseInfo().grade() > 5) {
            throw new GeneralException(AuthException.INVALID_GRADE_BAD_REQUEST);
        }

        LocalDate birthDate = LocalDate.parse(response.baseInfo().birthDate(), DateTimeFormatter.ISO_LOCAL_DATE);
        if (birthDate.isAfter(LocalDate.now())) {
            throw new GeneralException(AuthException.INVALID_BIRTHDATE_BAD_REQUEST);
        }

        if (response.detailInfo().gpa() < 0.0 || response.detailInfo().gpa() > 4.5) {
            throw new GeneralException(AuthException.INVALID_GPA_BAD_REQUEST);
        }

        if (response.detailInfo().incomeBracket() < 1 || response.detailInfo().incomeBracket() > 10) {
            throw new GeneralException(AuthException.INVALID_INCOME_BAD_REQUEST);
        }

        UsersDetails usersDetails = UsersDetails.createUsersDetails(response, user);
        UsersInterests usersInterests = UsersInterests.createUsersInterests(response, user);

        usersDetailsRepository.save(usersDetails);
        usersInterestsRepository.save(usersInterests);

        DetailInfoResponse detailInfoResponse = new DetailInfoResponse(
                usersDetails.getGpa(),
                usersDetails.getIncomeBracket(),
                usersDetails.getIsBasicLiving(),
                usersDetails.getIsSecondLowest(),
                user.getId()
        );

        // 🌟 고수님의 원본 DTO 생성자 타입 명칭과 구조를 깨뜨리지 않도록 수정했습니다.
        // 🌟 ResponseDto 대신, 고수님이 원래 원했던 BaseInfoDto 바구니에 담아줍니다!
        BaseInfoDto baseInfoResponse = new BaseInfoDto(
                response.baseInfo().schoolName(),
                response.baseInfo().department(),
                response.baseInfo().grade(),
                response.baseInfo().residence(),
                java.time.LocalDate.parse(response.baseInfo().birthDate())
        );

        return new UsersSubmitInfoResponse(baseInfoResponse, detailInfoResponse);
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

    @Transactional
    public UsersInfoResponse updateUserInfo(String username, UsersSubmitInfoRequest request) {
        Users user = usersRepository.findByUsername(username);
        if (user == null) {
            throw new GeneralException(AuthException.WRONG_USER_FORM_BAD_REQUEST);
        }

        UsersDetails usersDetails = usersDetailsRepository.findByUserId(user)
                .orElseThrow(() -> new GeneralException(AuthException.WRONG_USER_FORM_BAD_REQUEST));

        UsersSubmitInfoRequest.BaseInfoDto baseReq = request.baseInfo();
        user.updateBaseInfo(
                baseReq.schoolName(),
                baseReq.department(),
                baseReq.grade(),
                baseReq.residence(),
                baseReq.birthDate()
        );

        UsersSubmitInfoRequest.DetailInfoDto detailReq = request.detailInfo();
        usersDetails.updateDetailInfo(
                detailReq.gpa(),
                detailReq.incomeBracket(),
                detailReq.isBasicLiving(),
                detailReq.isSecondLowest()
        );

        // 🌟 고수님의 원래 대문자 .Interests() 객체에서 각각의 항목을 안전하게 체크하도록 수정했습니다.
        if (detailReq.Interests() != null) {
            usersInterestsRepository.deleteByUser(user);

            InterestsDto interestsDto = detailReq.Interests();

            if (Boolean.TRUE.equals(interestsDto.corporate())) {
                saveNewInterest(user, "corporate");
            }
            if (Boolean.TRUE.equals(interestsDto.region())) {
                saveNewInterest(user, "region");
            }
            if (Boolean.TRUE.equals(interestsDto.requirements())) {
                saveNewInterest(user, "requirements");
            }
            if (Boolean.TRUE.equals(interestsDto.state())) {
                saveNewInterest(user, "state");
            }
        }

        return getUserInfo(username);
    }

    private void saveNewInterest(Users user, String category) {
        UsersInterests interests = new UsersInterests();
        interests.setUser(user);
        interests.setCategory(category);
        usersInterestsRepository.save(interests);
    }
}