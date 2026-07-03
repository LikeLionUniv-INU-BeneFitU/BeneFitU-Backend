package com.fitu.benefitu.mockapi.auth;

import com.fitu.benefitu.global.error.code.BaseErrorCode;
import org.springframework.http.HttpStatus;

public enum AuthErrorCode implements BaseErrorCode {
    DUPLICATE_USERNAME(HttpStatus.BAD_REQUEST, "AUTH_4001", "이미 존재하는 아이디입니다."),
    INVALID_PASSWORD_FORMAT(HttpStatus.BAD_REQUEST, "AUTH_4002", "비밀번호는 영문, 숫자, 특수문자를 포함한 8자 이상이어야 합니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    AuthErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    @Override public HttpStatus getStatus() { return status; }
    @Override public String getCode() { return code; }
    @Override public String getMessage() { return message; }
}
