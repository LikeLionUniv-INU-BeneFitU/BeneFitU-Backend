package com.fitu.benefitu.domain.users.service;

import com.fitu.benefitu.domain.users.dto.AuthSignupRequest;
import com.fitu.benefitu.domain.users.dto.AuthSignupResponse;
import com.fitu.benefitu.domain.users.errors.AuthException;
import com.fitu.benefitu.domain.users.entity.Users;
import com.fitu.benefitu.domain.users.repository.UsersRepository;
import com.fitu.benefitu.global.error.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UsersService {
    private final UsersRepository usersRepository;

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

    //중복된 사용자에 대한 검증
    private void checkUsernameExists(String username) {
        Users user = usersRepository.findByUsername(username);
        if (user == null) {
            throw new GeneralException(AuthException.ALREADY_EXIST_USER_ID_BAD_REQUEST);
        }
    }
    //ID/PW 형식 검증
    private void checkUserIdAndPassword(String username, String password) {
        Users user = usersRepository.findByUsername(username);
        //ID 검증
        if (user.equals("")){
            throw new GeneralException(AuthException.WRONG_USER_FORM_BAD_REQUEST);
        }
        //PW 검증
        if (password.length() < 8){
            throw new GeneralException(AuthException.WRONG_USER_FORM_BAD_REQUEST);
        }
    }

}
