package com.fitu.benefitu.domain.users.service;

import com.fitu.benefitu.domain.users.dto.DetailInfoResponse;
import com.fitu.benefitu.domain.users.dto.UsersInfoSubmitRequest;
import com.fitu.benefitu.domain.users.dto.UsersInfoSubmitResponse;
import com.fitu.benefitu.domain.users.entity.Users;
import com.fitu.benefitu.domain.users.entity.UsersDetails;
import com.fitu.benefitu.domain.users.entity.UsersInterests;
import com.fitu.benefitu.domain.users.errors.UsersException;
import com.fitu.benefitu.domain.users.mapper.UserMapper;
import com.fitu.benefitu.domain.users.repository.UsersDetailsRepository;
import com.fitu.benefitu.domain.users.repository.UsersInterestsRepository;
import com.fitu.benefitu.domain.users.repository.UsersRepository;
import com.fitu.benefitu.global.error.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class UsersService {
    private final UsersRepository usersRepository;
    private final UsersDetailsRepository usersDetailsRepository;
    private final UsersInterestsRepository usersInterestsRepository;
    private final UserMapper userMapper;

    public UsersInfoSubmitResponse SubmitInfo(UsersInfoSubmitRequest usersInfoSubmitRequest, Long userId) {
        Users user = usersRepository.findById(userId).orElseThrow();
        if(user.getHasDetails()) {
            throw new GeneralException(UsersException.ALREADY_INSERTED_CONFLICT);
        }
            user = userMapper.toUsers(usersInfoSubmitRequest.baseInfo(), user);
            user.setUsersHasDetails(true);

            UsersDetails usersDetails = new UsersDetails();
            usersDetails = userMapper.toUserDetails(usersInfoSubmitRequest.detailInfo(), usersDetails);
            usersDetails.setOwner(user);

            List<UsersInterests> usersInterests = userMapper.toInterests(usersInfoSubmitRequest.detailInfo().interests(), user);

            usersDetailsRepository.save(usersDetails);
            usersInterestsRepository.saveAll(usersInterests);

            List<String> interestsResponses = usersInterests.stream().map(a ->a.getCategory().getDescription()).toList();
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
}
