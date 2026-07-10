package com.fitu.benefitu.domain.users.service;

import com.fitu.benefitu.domain.auth.SecurityUtil;
import com.fitu.benefitu.domain.users.dto.BaseInfoDto;
import com.fitu.benefitu.domain.users.dto.DetailInfoResponse;
import com.fitu.benefitu.domain.users.dto.UsersInfoSubmitRequest;
import com.fitu.benefitu.domain.users.dto.UsersInfoSubmitResponse;
import com.fitu.benefitu.domain.users.entity.Users;
import com.fitu.benefitu.domain.users.entity.UsersDetails;
import com.fitu.benefitu.domain.users.entity.UsersInterests;
import com.fitu.benefitu.domain.users.errors.UsersException;
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

    public UsersInfoSubmitResponse SubmitInfo(UsersInfoSubmitRequest usersInfoSubmitRequest) {
        Long userId = SecurityUtil.getCurrentUserId();
        Users user = usersRepository.findById(userId).orElseThrow();
        if (user.getHasDetails()) {
            throw new GeneralException(UsersException.ALREADY_INSERTED_CONFLICT);
        }
        user.toSubmittedUsers(usersInfoSubmitRequest.baseInfo());

        UsersDetails usersDetails = UsersDetails.createUserDetails(usersInfoSubmitRequest.detailInfo(), user);
        usersDetails.setOwner(user);

        List<UsersInterests> usersInterests = UsersInterests.toInterests(usersInfoSubmitRequest.detailInfo().interests(), user);

        usersDetailsRepository.save(usersDetails);
        usersInterestsRepository.saveAll(usersInterests);

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

    public UsersInfoSubmitResponse getInfo(){
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
}
