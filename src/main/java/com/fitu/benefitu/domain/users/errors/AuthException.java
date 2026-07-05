package com.fitu.benefitu.domain.users.errors;

import com.fitu.benefitu.global.error.code.BaseErrorCode;
import org.springframework.http.HttpStatus;

public enum AuthException implements BaseErrorCode {
    ALREADY_EXIST_USER_ID_BAD_REQUEST("AUTH_4001", "이미 존재하는 아이디입니다.",HttpStatus.BAD_REQUEST),
    WRONG_USER_FORM_BAD_REQUEST("AUTH_4002","비밀번호는 영문, 숫자, 특수문자를 포함한 8자 이상이어야 합니다.",HttpStatus.BAD_REQUEST),
    ;

    private String code;
    private String message;
    private HttpStatus httpStatus;

    AuthException(String code, String message, HttpStatus httpStatus){
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    @Override
    public String getCode() {
        return "";
    }

    @Override
    public String getMessage() {
        return "";
    }

    @Override
    public HttpStatus getStatus() {
        return null;
    }
}
