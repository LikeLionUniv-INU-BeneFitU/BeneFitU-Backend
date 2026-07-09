package com.fitu.benefitu.domain.auth.service;

import com.fitu.benefitu.domain.auth.errors.AuthException;
import com.fitu.benefitu.domain.users.entity.Users;
import com.fitu.benefitu.domain.users.repository.UsersRepository;
import com.fitu.benefitu.global.error.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = usersRepository.findByUsername(username);
        // username 존재 여부 확인
        if (user == null) {
            throw new GeneralException(AuthException.DID_NOT_MATCH_ID_AND_PW);
        }
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles("USER")
                .build();
    }
}

