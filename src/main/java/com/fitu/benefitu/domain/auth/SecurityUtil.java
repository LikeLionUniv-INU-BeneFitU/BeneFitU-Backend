package com.fitu.benefitu.domain.auth;

import com.fitu.benefitu.domain.auth.errors.AuthException;
import com.fitu.benefitu.global.error.exception.GeneralException;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {
    public static Long getCurrentUserId() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getDetails() == null) {
            throw new GeneralException(AuthException.NOT_VALID_TOKEN_UNAUTHORIZED); // 적절한 예외 처리
        }
        return (Long) authentication.getDetails();
    }
}
