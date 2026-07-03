package com.fitu.benefitu.mockapi.users;

import com.fitu.benefitu.global.error.code.BaseErrorCode;
import org.springframework.http.HttpStatus;

public enum UsersErrorCode implements BaseErrorCode {
    USER_ALREADY_REGISTERED(HttpStatus.CONFLICT, "USER_409", "이미 기본 정보를 입력하셨습니다."),

    INVALID_USER_DETAIL(HttpStatus.BAD_REQUEST, "USER_4002", "입력하신 상세 정보에 유효하지 않은 값이 포함되어 있습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

    UsersErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    @Override public HttpStatus getStatus() { return status; }
    @Override public String getCode() { return code; }
    @Override public String getMessage() { return message; }
}