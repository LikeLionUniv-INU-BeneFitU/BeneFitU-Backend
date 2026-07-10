package com.fitu.benefitu.domain.auth.service;

import com.fitu.benefitu.domain.auth.dto.AuthSignupRequest;
import com.fitu.benefitu.domain.auth.dto.AuthSignupResponse;
import com.fitu.benefitu.domain.users.entity.Users;
import com.fitu.benefitu.domain.users.errors.UsersException;
import com.fitu.benefitu.domain.users.repository.UsersRepository;
import com.fitu.benefitu.global.error.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthSignupResponse signup(AuthSignupRequest request) {
        //1. 검증
        checkUsernameExists(request.username());
        checkUserIdAndPassword(request.username(), request.password());
        String encodedPassword = passwordEncoder.encode(request.password());

        //2. 저장
        //Users 객체 생성
        Users user = Users.createUsers(request.username(), encodedPassword);
        //DB 저장
        usersRepository.save(user);

        //3. 반환
        return new AuthSignupResponse(user.getUsername());
    }

    // 중복된 사용자에 대한 검증 (수정됨: null이 아닐 때 에러)
    private void checkUsernameExists(String username) {
        Users user = usersRepository.findByUsername(username);
        if (user != null) { // 유저가 존재하면 중복이므로 에러!
            throw new GeneralException(UsersException.ALREADY_EXIST_USER_ID_BAD_REQUEST);
        }
    }

    // ID/PW 형식 검증
    private void checkUserIdAndPassword(String username, String password) {
        // 영문, 숫자, 특수문자를 포함한 8자 이상 정규표현식
        String passwordPattern = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$";

        // ID 검증 (빈 문자열 체크)
        if (username == null || username.trim().isEmpty()) {
            throw new GeneralException(UsersException.WRONG_USER_FORM_BAD_REQUEST);
        }

        // PW 검증 (길이 + 정규식 체크)
        if (password == null || !password.matches(passwordPattern)) {
            throw new GeneralException(UsersException.WRONG_USER_FORM_BAD_REQUEST);
        }
    }
}
