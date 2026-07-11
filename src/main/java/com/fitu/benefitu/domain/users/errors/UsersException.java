package com.fitu.benefitu.domain.users.errors;

import com.fitu.benefitu.global.error.code.BaseErrorCode;
import org.springframework.http.HttpStatus;

public enum UsersException implements BaseErrorCode {
    INVALID_INFO_BAD_REQUEST("USER_4002", "입력하신 상세 정보에 유효하지 않은 값이 포함되어 있습니다.", HttpStatus.BAD_REQUEST),
    ALREADY_INSERTED_CONFLICT("USER_409", "이미 기본 정보를 입력하셨습니다.", HttpStatus.CONFLICT);

    private String code;
    private String message;
    private HttpStatus httpStatus;

    UsersException(String code, String message, HttpStatus httpStatus){
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public HttpStatus getStatus() {
        return httpStatus;
    }
}
