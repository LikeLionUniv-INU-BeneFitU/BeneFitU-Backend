package com.fitu.benefitu.mockapi.auth;

import com.fitu.benefitu.global.error.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MockCustomUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username.equals("already")) {
            throw new GeneralException(AuthErrorCode.DUPLICATE_USERNAME);
        } else if (username.equals("wrong-form")) {
            throw new GeneralException(AuthErrorCode.INVALID_PASSWORD_FORMAT);
        }

        // 아이디로 아무거나 넣어도 무조건 통과시키고, 비밀번호는 0000로 고정하는 임시 코드
        return org.springframework.security.core.userdetails.User.builder()
                .username(username)
                .password("0000")
                .roles("USER")
                .build();
    }
}

